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
        @DisplayName("Deve lançar exceção se valor tiver mais de duas casas decimais")
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


    @Nested
    class ContaServiceTaxaTest
    {
        private ContaService contaService;

        @BeforeEach
        void setup()
        {
            contaService = new ContaService(null);
        }

        @Test
        @DisplayName("Deve calcular taxa zero para Pix")
        void deveCalcularTaxaZeroPix()
        {
            BigDecimal valor = new BigDecimal("100.00");
            BigDecimal taxa  = contaService.calcularTaxa("P", valor);

            assertEquals(new BigDecimal("0.00"), taxa);
        }

        @Test
        @DisplayName("Deve calcular taxa de 3% para débito")
        void deveCalcularTaxaDebito()
        {
            BigDecimal valor = new BigDecimal("100.00");
            BigDecimal taxa  = contaService.calcularTaxa("D", valor);

            assertEquals(new BigDecimal("3.00"), taxa);
        }

        @Test
        @DisplayName("Deve calcular taxa de 5% para crédito")
        void deveCalcularTaxaCredito()
        {
            BigDecimal valor = new BigDecimal("100.00");
            BigDecimal taxa  = contaService.calcularTaxa("C", valor);

            assertEquals(new BigDecimal("5.00"), taxa);
        }

        @Test
        @DisplayName("Deve arredondar valor total para duas casas decimais")
        void deveArredondarValorTotal()
        {
            BigDecimal valor = new BigDecimal("10.333");
            BigDecimal taxa  = contaService.calcularTaxa("D", valor);
            BigDecimal total = valor.add(taxa).setScale(2, BigDecimal.ROUND_HALF_UP);

            assertEquals(new BigDecimal("10.33"), valor.setScale(2, BigDecimal.ROUND_HALF_UP));
            assertEquals(new BigDecimal("0.31") , taxa);
            assertEquals(new BigDecimal("10.64"), total);
        }

        @Test
        @DisplayName("Deve calcular taxa correta para valores variados")
        void deveCalcularTaxaParaValoresVariados()
        {
            BigDecimal valor1 = new BigDecimal("123.45");
            BigDecimal valor2 = new BigDecimal("67.89");

            BigDecimal taxaDebito  = contaService.calcularTaxa("D", valor1);
            BigDecimal taxaCredito = contaService.calcularTaxa("C", valor2);

            assertEquals(new BigDecimal("3.70"), taxaDebito);
            assertEquals(new BigDecimal("3.39"), taxaCredito);
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

            when(contaRepository.buscarContaPorNumero(123)).thenReturn(conta);

            Conta resultado = contaService.consultarConta(123);

            assertEquals(123, resultado.getNumeroConta());
            assertEquals(new BigDecimal("100.00"), resultado.getSaldo());
        }

        @Test
        @DisplayName("Deve lançar exceção ao consultar conta inexistente")
        void consultarContaInexistente()
        {
            when(contaRepository.buscarContaPorNumero(123)).thenReturn(null);

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
            when(contaRepository.existeConta(123)).thenReturn(false);
            when(contaRepository.salvar(any(Conta.class))).thenAnswer(inv -> inv.getArgument(0));

            Conta conta = contaService.criarConta(123, BigDecimal.ZERO);

            assertEquals(123, conta.getNumeroConta());
            assertEquals(BigDecimal.ZERO.setScale(2), conta.getSaldo());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar criar conta com saldo negativo mínimo (-0.01)")
        void criarContaComSaldoNegativoMinimo()
        {
            assertThrows(SaldoNegativoException.class, () -> contaService.criarConta(123, new BigDecimal("-0.01")));
        }


        @Test
        @DisplayName("Deve aceitar transação com valor mínimo positivo")
        void transacaoComValorMinimoPositivo()
        {
            Conta conta = new Conta(123, new BigDecimal("1.00"));
            when(contaRepository.buscarContaPorNumero(123)).thenReturn(conta);
            when(contaRepository.salvar(any(Conta.class))).thenAnswer(i -> i.getArgument(0));

            Conta atualizada = contaService.realizarTransacao(123, "P", new BigDecimal("0.01"));

            assertEquals(new BigDecimal("0.99"), atualizada.getSaldo());
        }

        @Test
        @DisplayName("Deve lançar exceção para transação com valor menor que mínimo permitido (0.01)")
        void transacaoComValorAbaixoDoMinimo()
        {
            Conta conta = new Conta(123, new BigDecimal("1.00"));
            when(contaRepository.buscarContaPorNumero(123)).thenReturn(conta);

            assertThrows(ValorTransacaoInvalidoException.class, () -> contaService.realizarTransacao(123, "P", new BigDecimal("0.00")));
        }


        @Test
        @DisplayName("Deve permitir transação que consome exatamente todo o saldo com taxa")
        void transacaoComSaldoExatoComTaxa()
        {
            Conta conta = new Conta(123, new BigDecimal("10.30"));

            when(contaRepository.buscarContaPorNumero(123)).thenReturn(conta);
            when(contaRepository.salvar(any(Conta.class))).thenAnswer(i -> i.getArgument(0));

            Conta atualizada = contaService.realizarTransacao(123, "D", new BigDecimal("10.00"));

            assertEquals(BigDecimal.ZERO.setScale(2), atualizada.getSaldo());
        }

        @Test
        @DisplayName("Deve lançar exceção se transação ultrapassar saldo por um centavo")
        void transacaoUltrapassaSaldoPorCentavo()
        {
            Conta conta = new Conta(123, new BigDecimal("10.29"));
            when(contaRepository.buscarContaPorNumero(123)).thenReturn(conta);

            assertThrows(SaldoInsuficienteException.class, () -> contaService.realizarTransacao(123, "D", new BigDecimal("10.00")));
        }

    }

    @Nested
    class ArredondamentoTest
    {
        @Test
        @DisplayName("Deve arredondar corretamente valores com meio centavo para cima")
        void arredondarMeioCentavoParaCima()
        {
            BigDecimal valor = new BigDecimal("0.005");
            BigDecimal arredondado = valor.setScale(2, BigDecimal.ROUND_HALF_UP);

            assertEquals(new BigDecimal("0.01"), arredondado);
        }

        @Test
        @DisplayName("Deve arredondar corretamente valores com meio centavo para baixo")
        void arredondarMeioCentavoParaBaixo()
        {
            BigDecimal valor = new BigDecimal("2.004");
            BigDecimal arredondado = valor.setScale(2, BigDecimal.ROUND_HALF_UP);

            assertEquals(new BigDecimal("2.00"), arredondado);
        }

        @Test
        @DisplayName("Deve manter duas casas decimais no resultado final")
        void manterDuasCasasDecimais()
        {
            BigDecimal valor = new BigDecimal("10.336");
            BigDecimal arredondado = valor.setScale(2, BigDecimal.ROUND_HALF_UP);

            assertEquals(new BigDecimal("10.34"), arredondado);
        }

        @Test
        @DisplayName("Deve manter valor com duas casas decimais sem alteração")
        void manterValorComDuasCasas()
        {
            BigDecimal valor = new BigDecimal("25.50");
            BigDecimal arredondado = valor.setScale(2, BigDecimal.ROUND_HALF_UP);

            assertEquals(valor, arredondado);
        }
    }
}
