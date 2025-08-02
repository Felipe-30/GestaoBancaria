package UnitTest;

import com.felipe.gestaoBancaria.exception.*;
import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.repository.ContaRepository;
import com.felipe.gestaoBancaria.service.ContaService;
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
import static org.mockito.Mockito.*;

class ContaServiceTest
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

    @Nested
    class CriarContaTests
    {
        @Test
        @DisplayName("Deve criar conta com saldo válido")
        void deveCriarContaComSaldoValido()
        {
            when(contaRepository.existeConta(123)).thenReturn(false);
            when(contaRepository.salvar(any(Conta.class))).thenAnswer(inv -> inv.getArgument(0));

            Conta conta = contaService.criarConta(123, new BigDecimal("100.00"));

            assertEquals(123, conta.getNumeroConta());
            assertEquals(new BigDecimal("100.00"), conta.getSaldo());
        }

        @Test
        @DisplayName("Deve lançar exceção se conta já existir")
        void deveLancarExcecaoSeContaExistir()
        {
            when(contaRepository.existeConta(123)).thenReturn(true);

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
    class TransacaoTests
    {
        @Test
        @DisplayName("Deve realizar transação com débito")
        void transacaoDebito()
        {
            Conta conta = new Conta(123, new BigDecimal("100.00"));
            when(contaRepository.buscarContaPorNumero(123)).thenReturn(conta);
            when(contaRepository.salvar(any(Conta.class))).thenAnswer(i -> i.getArgument(0));

            Conta atualizada = contaService.realizarTransacao(123, "D", new BigDecimal("10.00"));

            assertEquals(new BigDecimal("89.70"), atualizada.getSaldo());
        }

        @Test
        @DisplayName("Deve realizar transação com crédito")
        void transacaoCredito()
        {
            Conta conta = new Conta(123, new BigDecimal("100.00"));

            when(contaRepository.buscarContaPorNumero(123)).thenReturn(conta);
            when(contaRepository.salvar(any(Conta.class))).thenAnswer(i -> i.getArgument(0));

            Conta atualizada = contaService.realizarTransacao(123, "C", new BigDecimal("10.00"));

            assertEquals(new BigDecimal("89.50"), atualizada.getSaldo());
        }

        @Test
        @DisplayName("Deve realizar transação com pix")
        void transacaoPix()
        {
            Conta conta = new Conta(123, new BigDecimal("100.00"));

            when(contaRepository.buscarContaPorNumero(123)).thenReturn(conta);
            when(contaRepository.salvar(any(Conta.class))).thenAnswer(i -> i.getArgument(0));

            Conta atualizada = contaService.realizarTransacao(123, "P", new BigDecimal("10.00"));

            assertEquals(new BigDecimal("90.00"), atualizada.getSaldo());
        }

        @Test
        @DisplayName("Deve lançar exceção se conta não for encontrada")
        void contaNaoEncontrada()
        {
            when(contaRepository.buscarContaPorNumero(999)).thenReturn(null);

            assertThrows(ContaNaoEncontradaException.class, () -> contaService.realizarTransacao(999, "P", new BigDecimal("10.00")));
        }

        @Test
        @DisplayName("Deve lançar exceção se saldo for insuficiente")
        void saldoInsuficiente()
        {
            Conta conta = new Conta(123, new BigDecimal("5.00"));
            when(contaRepository.buscarContaPorNumero(123)).thenReturn(conta);

            assertThrows(SaldoInsuficienteException.class, () -> contaService.realizarTransacao(123, "D", new BigDecimal("10.00")));
        }

        @Test
        @DisplayName("Deve lançar exceção se valor da transação for nulo")
        void valorTransacaoNulo()
        {
            assertThrows(IllegalArgumentException.class, () -> contaService.realizarTransacao(123, "C", null));
        }

        @Test
        @DisplayName("Deve lançar exceção se valor da transação for negativo")
        void valorTransacaoNegativo()
        {
            assertThrows(ValorTransacaoInvalidoException.class, () -> contaService.realizarTransacao(123, "D", new BigDecimal("-1.00")));
        }

        @Test
        @DisplayName("Deve lançar exceção se valor tiver mais de duas casas")
        void valorMaisDeDuasCasas()
        {
            assertThrows(IllegalArgumentException.class, () -> contaService.realizarTransacao(123, "P", new BigDecimal("10.123")));
        }

        @Test
        @DisplayName("Deve lançar exceção para forma de pagamento inválida")
        void formaPagamentoInvalida()
        {
            assertThrows(FormaPagamentoInvalidaException.class, () -> contaService.realizarTransacao(123, "X", new BigDecimal("10.00")));
        }

        @Test
        @DisplayName("Deve lançar exceção para forma de pagamento nula")
        void formaPagamentoNula()
        {
            assertThrows(FormaPagamentoInvalidaException.class, () -> contaService.realizarTransacao(123, null, new BigDecimal("10.00")));
        }
    }
}
