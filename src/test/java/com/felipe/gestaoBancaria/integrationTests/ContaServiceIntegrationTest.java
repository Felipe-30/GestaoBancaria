package com.felipe.gestaoBancaria.integrationTests;

import com.felipe.gestaoBancaria.exception.ContaExistenteException;
import com.felipe.gestaoBancaria.exception.ContaNaoEncontradaException;
import com.felipe.gestaoBancaria.exception.SaldoNegativoException;
import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.service.ContaService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ContaServiceIntegrationTest
{
    @Autowired
    private ContaService contaService;


    @Test
    void deveCriarContaComSaldoValido()
    {
        int numeroConta = 10;
        BigDecimal saldoInicial = new BigDecimal("1000.00");

        Conta contaCriada = contaService.criarConta(numeroConta, saldoInicial);

        assertThat(contaCriada).isNotNull();
        assertThat(contaCriada.getNumeroConta()).isEqualTo(numeroConta);
        assertThat(contaCriada.getSaldo()).isEqualByComparingTo(saldoInicial);
    }

    @Test
    void naoDeveCriarContaComSaldoNegativo()
    {
        int numeroConta = 11;
        BigDecimal saldoInicial = new BigDecimal("-50.00");

        assertThatThrownBy(() -> contaService.criarConta(numeroConta, saldoInicial))
                .isInstanceOf(SaldoNegativoException.class)
                .hasMessageContaining("não pode ser criada com saldo negativo.");
    }

    @Test
    void naoDeveCriarContaQuandoNumeroJaExistir()
    {
        int numeroConta = 12;
        BigDecimal saldoInicial = new BigDecimal("200.00");

        contaService.criarConta(numeroConta, saldoInicial);

        assertThatThrownBy(() -> contaService.criarConta(numeroConta, saldoInicial))
                .isInstanceOf(ContaExistenteException.class)
                .hasMessageContaining("A conta de número: " + numeroConta + " já existe na base de dados.");
    }

    @Test
    void naoDeveCriarContaComSaldoInicialNulo()
    {
        int numeroConta = 13;

        assertThatThrownBy(() -> contaService.criarConta(numeroConta, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Saldo inicial não pode ser nulo.");
    }

    @Test
    void naoDeveCriarContaComSaldoComMaisDeDuasCasasDecimais()
    {
        int numeroConta = 14;
        BigDecimal saldoInicial = new BigDecimal("100.123");

        assertThatThrownBy(() -> contaService.criarConta(numeroConta, saldoInicial))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Saldo inicial deve ter no máximo 2 casas decimais.");
    }

    @Test
    void deveConsultarContaExistente()
    {
        int numeroConta = 15;
        BigDecimal saldoInicial = new BigDecimal("500.00");

        contaService.criarConta(numeroConta, saldoInicial);

        Conta conta = contaService.consultarConta(numeroConta);

        assertThat(conta).isNotNull();
        assertThat(conta.getNumeroConta()).isEqualTo(numeroConta);
        assertThat(conta.getSaldo()).isEqualByComparingTo(saldoInicial);
    }

    @Test
    void deveLancarExcecaoAoConsultarContaInexistente()
    {
        int numeroContaInexistente = 9999;

        assertThatThrownBy(() -> contaService.consultarConta(numeroContaInexistente))
                .isInstanceOf(ContaNaoEncontradaException.class)
                .hasMessageContaining("A conta de número: " + numeroContaInexistente + " não foi encontrada na base de dados.");
    }

    @Test
    void deveCriarContaComSaldoZero()
    {
        int numeroConta = 20;
        BigDecimal saldoInicial = BigDecimal.ZERO;

        Conta contaCriada = contaService.criarConta(numeroConta, saldoInicial);

        assertThat(contaCriada).isNotNull();
        assertThat(contaCriada.getNumeroConta()).isEqualTo(numeroConta);
        assertThat(contaCriada.getSaldo()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void naoDeveCriarContaComNumeroContaNegativo()
    {
        int numeroContaNegativo = -1;
        BigDecimal saldoInicial = new BigDecimal("100.00");

        assertThatThrownBy(() -> contaService.criarConta(numeroContaNegativo, saldoInicial))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Número da conta deve ser maior que zero.");
    }
}
