package com.felipe.gestaoBancaria.unitTests;

import com.felipe.gestaoBancaria.exception.ContaExistenteException;
import com.felipe.gestaoBancaria.exception.ContaNaoEncontradaException;
import com.felipe.gestaoBancaria.exception.SaldoNegativoException;
import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.repository.ContaRepository;
import com.felipe.gestaoBancaria.service.ContaService;
import com.felipe.gestaoBancaria.utils.BigDecimalUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ContaServiceUnitTest
{
    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    private void mockContaNaoExistenteComSalvar()
    {
        when(contaRepository.existsByNumeroConta(123)).thenReturn(false);
        when(contaRepository.save(any(Conta.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Nested
    class CriarContaTests
    {
        @Test
        @DisplayName("Deve criar conta com saldo válido")
        void deveCriarContaComSaldoValido()
        {
            mockContaNaoExistenteComSalvar();

            Conta conta = contaService.criarConta(123, new BigDecimal("100.00"));

            assertEquals(123, conta.getNumeroConta());
            assertEquals(BigDecimalUtils.arredondar(new BigDecimal("100.00")), conta.getSaldo());
        }

        @Test
        @DisplayName("Deve lançar exceção se conta já existir")
        void deveLancarExcecaoSeContaExistir()
        {
            when(contaRepository.existsByNumeroConta(123)).thenReturn(true);

            assertThrows(ContaExistenteException.class, () -> contaService.criarConta(123, new BigDecimal("100.00")));
        }

        @Test
        @DisplayName("Deve lançar exceção se saldo inicial for negativo")
        void saldoNegativo()
        {
            assertThrows(SaldoNegativoException.class, () -> contaService.criarConta(123, new BigDecimal("-10.00")));
        }

        @Test
        @DisplayName("Deve lançar exceção se saldo inicial for nulo")
        void saldoNulo()
        {
            assertThrows(IllegalArgumentException.class, () -> contaService.criarConta(123, null));
        }

        @Test
        @DisplayName("Deve lançar exceção se saldo tiver mais de duas casas decimais")
        void saldoComMaisDeDuasCasasDecimais()
        {
            assertThrows(IllegalArgumentException.class, () -> contaService.criarConta(123, new BigDecimal("10.123")));
        }
    }

    @Nested
    class ContaServiceConsultaTest
    {
        @Test
        @DisplayName("Deve consultar conta existente com sucesso")
        void consultarContaComSucesso()
        {
            Conta conta = new Conta(123, new BigDecimal("100.00"));

            when(contaRepository.findByNumeroConta(123)).thenReturn(conta);

            Conta resultado = contaService.consultarConta(123);

            assertEquals(123, resultado.getNumeroConta());
            assertEquals(BigDecimalUtils.arredondar(new BigDecimal("100.00")), resultado.getSaldo());
        }

        @Test
        @DisplayName("Deve lançar exceção ao consultar conta inexistente")
        void consultarContaInexistente()
        {
            when(contaRepository.findByNumeroConta(123)).thenReturn(null);

            assertThrows(ContaNaoEncontradaException.class, () -> contaService.consultarConta(123));
        }
    }

    @Nested
    class ContaServiceLimitesValidosTest
    {
        @Test
        @DisplayName("Deve criar conta com saldo zero")
        void criarContaComSaldoZero()
        {
            mockContaNaoExistenteComSalvar();

            Conta conta = contaService.criarConta(123, BigDecimal.ZERO);

            assertEquals(123, conta.getNumeroConta());
            assertEquals(0, conta.getSaldo().compareTo(BigDecimal.ZERO.setScale(2)));
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar criar conta com saldo negativo mínimo (-0.01)")
        void criarContaComSaldoNegativoMinimo()
        {
            assertThrows(SaldoNegativoException.class, () -> contaService.criarConta(123, new BigDecimal("-0.01")));
        }
    }
}
