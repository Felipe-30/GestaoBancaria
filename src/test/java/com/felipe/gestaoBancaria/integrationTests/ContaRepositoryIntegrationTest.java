package com.felipe.gestaoBancaria.integrationTests;

import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.repository.ContaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class ContaRepositoryIntegrationTest
{
    @Autowired
    private ContaRepository contaRepository;

    @Test
    void deveSalvarEncontrarContaPorNumero()
    {
        Conta conta = new Conta(555, new BigDecimal("1500.00"));

        contaRepository.save(conta);
        Conta encontrada = contaRepository.findByNumeroConta(555);

        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getNumeroConta()).isEqualTo(555);
        assertThat(encontrada.getSaldo()).isEqualByComparingTo("1500.00");
    }

    @Test
    void deveRetornarNullParaContaInexistente()
    {
        Conta encontrada = contaRepository.findByNumeroConta(99999);
        assertThat(encontrada).isNull();
    }

    @Test
    void deveRetornarTrueParaContaExistente()
    {
        Conta conta = new Conta(123, new BigDecimal("500.00"));
        contaRepository.save(conta);

        boolean existe = contaRepository.existsByNumeroConta(123);

        assertThat(existe).isTrue();
    }

    @Test
    void deveRetornarFalseParaContaInexistente()
    {
        boolean existe = contaRepository.existsByNumeroConta(88888);
        assertThat(existe).isFalse();
    }

    @Test
    void devePersistirMultiplasContasEEncontrarCadaUmaPorNumero()
    {
        Conta conta1 = new Conta(1, new BigDecimal("100.00"));
        Conta conta2 = new Conta(2, new BigDecimal("200.00"));
        Conta conta3 = new Conta(3, new BigDecimal("300.00"));

        contaRepository.save(conta1);
        contaRepository.save(conta2);
        contaRepository.save(conta3);

        assertThat(contaRepository.findByNumeroConta(1)).isNotNull();
        assertThat(contaRepository.findByNumeroConta(1).getSaldo()).isEqualByComparingTo("100.00");

        assertThat(contaRepository.findByNumeroConta(2)).isNotNull();
        assertThat(contaRepository.findByNumeroConta(2).getSaldo()).isEqualByComparingTo("200.00");

        assertThat(contaRepository.findByNumeroConta(3)).isNotNull();
        assertThat(contaRepository.findByNumeroConta(3).getSaldo()).isEqualByComparingTo("300.00");
    }
}
