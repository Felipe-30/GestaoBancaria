package com.felipe.gestaoBancaria.integrationTests;

import com.felipe.gestaoBancaria.dto.TransacaoResponseDTO;
import com.felipe.gestaoBancaria.exception.FormaPagamentoInvalidaException;
import com.felipe.gestaoBancaria.exception.SaldoInsuficienteException;
import com.felipe.gestaoBancaria.exception.ValorTransacaoInvalidoException;
import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TransacaoServiceIntegrationTest
{
    @Autowired
    private TransacaoService transacaoService;


    @Test
    void deveRealizarTransacaoPix() {
        Conta conta = transacaoService.realizarTransacao(100, "P", new BigDecimal("100.00"));

        assertThat(conta).isNotNull();
        assertThat(conta.getNumeroConta()).isEqualTo(100);
        assertThat(conta.getSaldo()).isEqualTo(new BigDecimal("400.00"));
    }

    @Test
    void deveRealizarTransacaoDebitoComTaxa()
    {
        Conta conta = transacaoService.realizarTransacao(100, "D", new BigDecimal("100.00"));

        BigDecimal taxa = new BigDecimal("3.00");
        BigDecimal total = new BigDecimal("103.00");
        BigDecimal saldoEsperado = new BigDecimal("500.00").subtract(total);

        assertThat(conta).isNotNull();
        assertThat(conta.getSaldo()).isEqualByComparingTo(saldoEsperado);
    }

    @Test
    void deveRealizarTransacaoCreditoComTaxa()
    {
        Conta conta = transacaoService.realizarTransacao(100, "C", new BigDecimal("100.00"));

        BigDecimal taxa = new BigDecimal("5.00");
        BigDecimal total = new BigDecimal("105.00");
        BigDecimal saldoEsperado = new BigDecimal("500.00").subtract(total);

        assertThat(conta).isNotNull();
        assertThat(conta.getSaldo()).isEqualByComparingTo(saldoEsperado);
    }

    @Test
    void deveBuscarTransacoesPorConta()
    {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarTransacoesPorConta(102);

        assertThat(transacoes).isNotEmpty();
        assertThat(transacoes).allMatch(t -> t.getNumeroConta() == 102);
    }

    @Test
    void deveBuscarTransacoesPorFormaPagamento()
    {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarTransacoesPorFormaPagamento(102, "D");

        assertThat(transacoes).isNotEmpty();
        assertThat(transacoes).allMatch(t -> t.getFormaPagamento().equals("D"));
    }

    @Test
    void deveBuscarTransacoesComValorMaiorQue()
    {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarTransacoesPorValorMaiorQue(104, new BigDecimal("150"));

        assertThat(transacoes).isNotEmpty();
        assertThat(transacoes).allMatch(t -> t.getValor().compareTo(new BigDecimal("150")) > 0);
    }

    @Test
    void deveBuscarTransacoesOrdenadasPorData()
    {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarTransacoesOrdenadasPorData(104);

        assertThat(transacoes).isNotEmpty();

        List<LocalDateTime> datas = transacoes.stream()
                .map(TransacaoResponseDTO::getDataTransacao)
                .toList();

        List<LocalDateTime> ordenadas = datas.stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        assertThat(datas).isEqualTo(ordenadas);
    }

    @Test
    void deveBuscarTransacoesPorIntervaloDeDatas()
    {
        LocalDateTime inicio = LocalDateTime.now().minusDays(15);
        LocalDateTime fim = LocalDateTime.now();

        List<TransacaoResponseDTO> transacoes = transacaoService.buscarTransacoesPorIntervaloDeDatas(105, inicio, fim);

        assertThat(transacoes).isNotEmpty();
        assertThat(transacoes).allMatch(t ->
                !t.getDataTransacao().isBefore(inicio) &&
                        !t.getDataTransacao().isAfter(fim)
        );
    }

    @Test
    void deveRetornarListaVaziaParaContaInexistente()
    {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarTransacoesPorConta(99999);

        assertThat(transacoes).isEmpty();
    }

    @Test
    void deveLancarExcecaoParaFormaPagamentoInvalida()
    {
        assertThrows(FormaPagamentoInvalidaException.class,
                () -> transacaoService.realizarTransacao(100, "X", BigDecimal.TEN));
    }

    @Test
    void deveLancarExcecaoParaValorNulo()
    {
        assertThrows(IllegalArgumentException.class,
                () -> transacaoService.realizarTransacao(100, "P", null));
    }

    @Test
    void deveLancarExcecaoParaValorComMaisDeDuasCasasDecimais()
    {
        assertThrows(IllegalArgumentException.class,
                () -> transacaoService.realizarTransacao(100, "P", new BigDecimal("10.123")));
    }

    @Test
    void deveLancarExcecaoParaValorMenorOuIgualZero()
    {
        assertThrows(ValorTransacaoInvalidoException.class,
                () -> transacaoService.realizarTransacao(100, "P", BigDecimal.ZERO));

        assertThrows(ValorTransacaoInvalidoException.class,
                () -> transacaoService.realizarTransacao(100, "P", new BigDecimal("-1")));
    }

    @Test
    void deveLancarExcecaoParaSaldoInsuficiente()
    {
        assertThrows(SaldoInsuficienteException.class,
                () -> transacaoService.realizarTransacao(100, "P", new BigDecimal("1000000")));
    }
}
