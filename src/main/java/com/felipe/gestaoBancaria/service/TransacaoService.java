package com.felipe.gestaoBancaria.service;

import com.felipe.gestaoBancaria.dto.TransacaoResponseDTO;
import com.felipe.gestaoBancaria.exception.FormaPagamentoInvalidaException;
import com.felipe.gestaoBancaria.exception.SaldoInsuficienteException;
import com.felipe.gestaoBancaria.exception.ValorTransacaoInvalidoException;
import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.model.Transacao;
import com.felipe.gestaoBancaria.repository.ContaRepository;
import com.felipe.gestaoBancaria.repository.TransacaoRepository;
import com.felipe.gestaoBancaria.utils.BigDecimalUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransacaoService
{
    private static final Map<String, BigDecimal> TAXA_POR_FORMA_PAGAMENTO = Map.of(
            "P", BigDecimal.ZERO, "D", new BigDecimal("0.03"), "C", new BigDecimal("0.05"));

    private final ContaService    contaService;
    private final ContaRepository contaRepository;

    private final TransacaoRepository transacaoRepository;

    public TransacaoService(ContaService contaService, ContaRepository contaRepository, TransacaoRepository transacaoRepository)
    {
        this.contaService        = contaService;
        this.contaRepository     = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public Conta realizarTransacao(int numeroConta, String formaPagamento, BigDecimal valor)
    {
        validarFormatoTransacao(formaPagamento, valor);

        Conta conta = contaService.consultarConta(numeroConta);

        BigDecimal taxa  = calcularTaxa(formaPagamento, valor);
        BigDecimal total = BigDecimalUtils.arredondar(valor.add(taxa));

        validarTransacao(conta, total);

        conta.debitar(total);

        Transacao transacao = new Transacao(conta, formaPagamento, valor, taxa, LocalDateTime.now());
        transacaoRepository.save(transacao);

        return contaRepository.save(conta);
    }

    public BigDecimal calcularTaxa(String formaPagamento, BigDecimal valor)
    {
        return BigDecimalUtils.arredondar(valor.multiply(TAXA_POR_FORMA_PAGAMENTO.get(formaPagamento)));
    }

    private static void validarFormatoTransacao(String formaPagamento, BigDecimal valor)
    {
        if (formaPagamento == null || !TAXA_POR_FORMA_PAGAMENTO.containsKey(formaPagamento))
        {
            String formasAceitas = String.join(", ", TAXA_POR_FORMA_PAGAMENTO.keySet());

            throw new FormaPagamentoInvalidaException("Formas aceitas: " + formasAceitas);
        }

        if (valor == null)
        {
            throw new IllegalArgumentException("O valor da transação não pode ser nulo.");
        }

        if (valor.scale() > 2)
        {
            throw new IllegalArgumentException("O valor da transação deve ter no máximo duas casas decimais.");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ValorTransacaoInvalidoException("O valor de qualquer transação deve ser maior que zero.");
        }
    }

    private static void validarTransacao(Conta conta, BigDecimal total)
    {
        if (conta.getSaldo().compareTo(total) < 0)
        {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a transação.");
        }
    }

    public List<TransacaoResponseDTO> buscarTransacoesPorConta(int numeroConta)
    {
        return toDTO(transacaoRepository.findByContaNumeroConta(numeroConta));
    }

    public List<TransacaoResponseDTO> buscarTransacoesPorFormaPagamento(int numeroConta, String formaPagamento)
    {
        return toDTO(transacaoRepository.findByContaNumeroContaAndFormaPagamento(numeroConta, formaPagamento));
    }

    public List<TransacaoResponseDTO> buscarTransacoesPorValorMaiorQue(int numeroConta, BigDecimal valorMinimo)
    {
        return toDTO(transacaoRepository.findByContaNumeroContaAndValorGreaterThan(numeroConta, valorMinimo));
    }

    public List<TransacaoResponseDTO> buscarTransacoesOrdenadasPorData(int numeroConta)
    {
        return toDTO(transacaoRepository.findByContaNumeroContaOrderByDataTransacaoDesc(numeroConta));
    }

    public List<TransacaoResponseDTO> buscarTransacoesPorIntervaloDeDatas(int numeroConta, LocalDateTime inicio, LocalDateTime fim)
    {
        return toDTO(transacaoRepository.findByContaNumeroContaAndDataTransacaoBetween(numeroConta, inicio, fim));
    }

    private List<TransacaoResponseDTO> toDTO(List<Transacao> transacoes)
    {
        return transacoes.stream()
                .map(t -> new TransacaoResponseDTO(
                        t.getFormaPagamento(),
                        t.getValor(),
                        t.getTaxa(),
                        t.getDataTransacao()
                ))
                .collect(Collectors.toList());
    }
}

