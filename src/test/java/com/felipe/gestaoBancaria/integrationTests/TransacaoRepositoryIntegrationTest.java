package com.felipe.gestaoBancaria.integrationTests;

import com.felipe.gestaoBancaria.model.Transacao;
import com.felipe.gestaoBancaria.repository.TransacaoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TransacaoRepositoryIntegrationTest
{
    @Autowired
    private TransacaoRepository transacaoRepository;

    @Test
    void deveRetornarTransacoesPorNumeroConta()
    {
        List<Transacao> transacoes = transacaoRepository.findByContaNumeroConta(102);

        assertThat(transacoes).isNotEmpty();
        assertThat(transacoes).allMatch(t -> t.getConta().getNumeroConta() == 102);
    }

    @Test
    void deveRetornarTransacoesPorContaEFormaPagamento()
    {
        List<Transacao> transacoes = transacaoRepository.findByContaNumeroContaAndFormaPagamento(102, "P");

        assertThat(transacoes).isNotEmpty();
        assertThat(transacoes).allMatch(t -> t.getFormaPagamento().equals("P"));
    }

    @Test
    void deveRetornarTransacoesPorContaEValorMaiorQue()
    {
        List<Transacao> transacoes = transacaoRepository.findByContaNumeroContaAndValorGreaterThan(104, new BigDecimal("100"));

        assertThat(transacoes).isNotEmpty();
        assertThat(transacoes).allMatch(t -> t.getValor().compareTo(new BigDecimal("100")) > 0);
    }

    @Test
    void deveRetornarTransacoesOrdenadasPorDataDecrescente() {
        List<Transacao> transacoes = transacaoRepository.findByContaNumeroContaOrderByDataTransacaoDesc(104);

        assertThat(transacoes).isNotEmpty();

        IntStream.range(1, transacoes.size())
                .forEach(i -> assertThat(transacoes.get(i - 1).getDataTransacao())
                        .isAfterOrEqualTo(transacoes.get(i).getDataTransacao()));
    }

    @Test
    void deveRetornarTransacoesPorIntervaloDeDatas()
    {
        LocalDateTime inicio = LocalDateTime.now().minusDays(15);
        LocalDateTime fim = LocalDateTime.now();

        List<Transacao> transacoes = transacaoRepository.findByContaNumeroContaAndDataTransacaoBetween(105, inicio, fim);

        assertThat(transacoes).isNotEmpty();
        assertThat(transacoes).allMatch(t -> !t.getDataTransacao().isBefore(inicio) && !t.getDataTransacao().isAfter(fim));
    }

    @Test
    void deveRetornarListaVaziaParaContaSemTransacoes()
    {
        List<Transacao> transacoes = transacaoRepository.findByContaNumeroConta(99999); // conta inexistente

        assertThat(transacoes).isEmpty();
    }
}
