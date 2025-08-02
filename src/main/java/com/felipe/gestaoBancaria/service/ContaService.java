package com.felipe.gestaoBancaria.service;

import com.felipe.gestaoBancaria.exception.*;
import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.repository.ContaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class ContaService
{
    private static final Map<String, BigDecimal> TAXA_POR_FORMA_PAGAMENTO = Map.of("P", BigDecimal.ZERO, "D", new BigDecimal("0.03"), "C", new BigDecimal("0.05"));

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository)
    {
        this.contaRepository = contaRepository;
    }

    public Conta criarConta(int numeroConta, BigDecimal saldoInicial)
    {
        validarNovaConta(numeroConta, saldoInicial);

        Conta conta = new Conta(numeroConta, arredondar(saldoInicial));

        return contaRepository.salvar(conta);
    }

    private void validarNovaConta(int numeroConta, BigDecimal saldoInicial)
    {
        validarSaldoInicial(saldoInicial);

        if (contaRepository.existeConta(numeroConta))
        {
            throw new ContaExistenteException("A conta de número: " + numeroConta + " já existe na base de dados.");
        }
    }

    private static void validarSaldoInicial(BigDecimal saldoInicial)
    {
        if (saldoInicial == null)
        {
            throw new IllegalArgumentException("Saldo inicial não pode ser nulo.");
        }

        if (saldoInicial.scale() > 2)
        {
            throw new IllegalArgumentException("Saldo inicial deve ter no máximo 2 casas decimais.");
        }

        if (saldoInicial.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new SaldoNegativoException("Uma conta não pode ser criada com saldo negativo.");
        }
    }


    public Conta realizarTransacao(int numeroConta, String formaPagamento, BigDecimal valor)
    {
        validarFormatoTransacao(formaPagamento, valor);

        Conta conta = consultarConta(numeroConta);

        BigDecimal taxa  = calcularTaxa(formaPagamento, valor);
        BigDecimal total = arredondar(valor.add(taxa));

        validarTransacao(conta, total);

        conta.debitar(total);

        return contaRepository.salvar(conta);
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

    public Conta consultarConta(int numeroConta)
    {
        Conta conta = contaRepository.buscarContaPorNumero(numeroConta);

        if (conta == null)
        {
            throw new ContaNaoEncontradaException("A conta de número: " + numeroConta + " não foi encontrada na base de dados.");
        }

        return conta;
    }

    private BigDecimal calcularTaxa(String formaPagamento, BigDecimal valor)
    {
        return arredondar(valor.multiply(TAXA_POR_FORMA_PAGAMENTO.get(formaPagamento)));
    }

    private static void validarTransacao(Conta conta, BigDecimal total)
    {
        if (conta.getSaldo().compareTo(total) < 0)
        {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a transação.");
        }
    }

    private static BigDecimal arredondar(BigDecimal valor)
    {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }
}
