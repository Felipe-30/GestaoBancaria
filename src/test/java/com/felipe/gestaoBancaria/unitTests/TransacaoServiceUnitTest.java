package com.felipe.gestaoBancaria.unitTests;

import com.felipe.gestaoBancaria.exception.*;
import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.repository.ContaRepository;
import com.felipe.gestaoBancaria.service.ContaService;
import com.felipe.gestaoBancaria.service.TransacaoService;
import com.felipe.gestaoBancaria.utils.BigDecimalUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransacaoServiceUnitTest
{
    @Mock private ContaRepository contaRepository;
    @Mock private ContaService contaService;

    @InjectMocks
    private TransacaoService transacaoService;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class TransacaoTests
    {
        @Test
        @DisplayName("Deve realizar transação com débito")
        void transacaoDebito()
        {
            Conta conta = new Conta(123, new BigDecimal("100.00"));

            when(contaService.consultarConta(123)).thenReturn(conta);
            when(contaRepository.save(any(Conta.class))).thenAnswer(i -> i.getArgument(0));

            Conta atualizada = transacaoService.realizarTransacao(123, "D", new BigDecimal("10.00"));

            assertEquals(new BigDecimal("89.70"), atualizada.getSaldo());
        }

        @Test
        @DisplayName("Deve realizar transação com crédito")
        void transacaoCredito()
        {
            Conta conta = new Conta(123, new BigDecimal("100.00"));

            when(contaService.consultarConta(123)).thenReturn(conta);
            when(contaRepository.save(any(Conta.class))).thenAnswer(i -> i.getArgument(0));

            Conta atualizada = transacaoService.realizarTransacao(123, "C", new BigDecimal("10.00"));

            assertEquals(new BigDecimal("89.50"), atualizada.getSaldo());
        }

        @Test
        @DisplayName("Deve realizar transação com pix")
        void transacaoPix()
        {
            Conta conta = new Conta(123, new BigDecimal("100.00"));

            when(contaService.consultarConta(123)).thenReturn(conta);
            when(contaRepository.save(any(Conta.class))).thenAnswer(i -> i.getArgument(0));

            Conta atualizada = transacaoService.realizarTransacao(123, "P", new BigDecimal("10.00"));

            assertEquals(new BigDecimal("90.00"), atualizada.getSaldo());
        }

        @Test
        @DisplayName("Deve lançar exceção se conta não for encontrada")
        void contaNaoEncontrada()
        {
            when(contaService.consultarConta(999)).thenThrow(new ContaNaoEncontradaException(""));

            assertThrows(ContaNaoEncontradaException.class, () ->
                    transacaoService.realizarTransacao(999, "P", new BigDecimal("10.00")));
        }

        @Test
        @DisplayName("Deve lançar exceção se saldo for insuficiente")
        void saldoInsuficiente()
        {
            Conta conta = new Conta(123, new BigDecimal("5.00"));

            when(contaService.consultarConta(123)).thenReturn(conta);

            assertThrows(SaldoInsuficienteException.class, () ->
                    transacaoService.realizarTransacao(123, "D", new BigDecimal("10.00")));
        }

        @Test
        @DisplayName("Deve lançar exceção se valor da transação for nulo")
        void valorTransacaoNulo()
        {
            assertThrows(IllegalArgumentException.class, () ->
                    transacaoService.realizarTransacao(123, "C", null));
        }

        @Test
        @DisplayName("Deve lançar exceção se valor da transação for negativo")
        void valorTransacaoNegativo()
        {
            assertThrows(ValorTransacaoInvalidoException.class, () ->
                    transacaoService.realizarTransacao(123, "D", new BigDecimal("-1.00")));
        }

        @Test
        @DisplayName("Deve lançar exceção se valor tiver mais de duas casas decimais")
        void valorMaisDeDuasCasas()
        {
            assertThrows(IllegalArgumentException.class, () ->
                    transacaoService.realizarTransacao(123, "P", new BigDecimal("10.123")));
        }

        @Test
        @DisplayName("Deve lançar exceção para forma de pagamento inválida")
        void formaPagamentoInvalida()
        {
            assertThrows(FormaPagamentoInvalidaException.class, () ->
                    transacaoService.realizarTransacao(123, "X", new BigDecimal("10.00")));
        }

        @Test
        @DisplayName("Deve lançar exceção para forma de pagamento nula")
        void formaPagamentoNula()
        {
            assertThrows(FormaPagamentoInvalidaException.class, () ->
                    transacaoService.realizarTransacao(123, null, new BigDecimal("10.00")));
        }
    }

    @Nested
    class CalculoTaxaTests
    {
        @Test
        @DisplayName("Deve calcular taxa zero para Pix")
        void taxaZeroParaPix()
        {
            assertEquals(BigDecimal.ZERO.setScale(2), transacaoService.calcularTaxa("P", new BigDecimal("100.00")));
        }

        @Test
        @DisplayName("Deve calcular taxa de 3% para Débito")
        void taxaDebito()
        {
            assertEquals(new BigDecimal("3.00"), transacaoService.calcularTaxa("D", new BigDecimal("100.00")));
        }

        @Test
        @DisplayName("Deve calcular taxa de 5% para Crédito")
        void taxaCredito()
        {
            assertEquals(new BigDecimal("5.00"), transacaoService.calcularTaxa("C", new BigDecimal("100.00")));
        }

        @Test
        @DisplayName("Deve arredondar valor total para duas casas decimais")
        void arredondamentoDoTotal()
        {
            BigDecimal valor = new BigDecimal("10.333");
            BigDecimal taxa  = transacaoService.calcularTaxa("D", valor);
            BigDecimal total = BigDecimalUtils.arredondar(valor.add(taxa));

            assertEquals(new BigDecimal("0.31"), taxa);
            assertEquals(new BigDecimal("10.64"), total);
        }

        @Test
        @DisplayName("Deve calcular taxa correta para valores variados")
        void taxasComValoresVariados()
        {
            assertEquals(new BigDecimal("3.70"), transacaoService.calcularTaxa("D", new BigDecimal("123.45")));
            assertEquals(new BigDecimal("3.39"), transacaoService.calcularTaxa("C", new BigDecimal("67.89")));
        }
    }

    @Nested
    class LimitesValidos
    {
        @Test
        @DisplayName("Transação mínima permitida (0.01)")
        void valorMinimoPermitido()
        {
            Conta conta = new Conta(123, new BigDecimal("1.00"));

            when(contaService.consultarConta(123)).thenReturn(conta);
            when(contaRepository.save(any(Conta.class))).thenAnswer(i -> i.getArgument(0));

            Conta atualizada = transacaoService.realizarTransacao(123, "P", new BigDecimal("0.01"));

            assertEquals(new BigDecimal("0.99"), atualizada.getSaldo());
        }

        @Test
        @DisplayName("Transação abaixo do mínimo (0.00)")
        void valorAbaixoMinimo()
        {
            Conta conta = new Conta(123, new BigDecimal("1.00"));

            when(contaService.consultarConta(123)).thenReturn(conta);

            assertThrows(ValorTransacaoInvalidoException.class, () ->
                    transacaoService.realizarTransacao(123, "P", new BigDecimal("0.00")));
        }

        @Test
        @DisplayName("Transação que consome todo o saldo com taxa")
        void saldoExatoComTaxa()
        {
            Conta conta = new Conta(123, new BigDecimal("10.30"));

            when(contaService.consultarConta(123)).thenReturn(conta);
            when(contaRepository.save(any(Conta.class))).thenAnswer(i -> i.getArgument(0));

            Conta atualizada = transacaoService.realizarTransacao(123, "D", new BigDecimal("10.00"));

            assertEquals(BigDecimal.ZERO.setScale(2), atualizada.getSaldo());
        }

        @Test
        @DisplayName("Transação que ultrapassa o saldo por 1 centavo")
        void saldoUltrapassadoPorCentavo()
        {
            Conta conta = new Conta(123, new BigDecimal("10.29"));

            when(contaService.consultarConta(123)).thenReturn(conta);

            assertThrows(SaldoInsuficienteException.class, () ->
                    transacaoService.realizarTransacao(123, "D", new BigDecimal("10.00")));
        }
    }
}
