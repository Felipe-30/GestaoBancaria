package com.felipe.gestaoBancaria.service;

import com.felipe.gestaoBancaria.exception.*;
import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.repository.ContaRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ContaService
{
    private static final Map<String, Double> TAXA_POR_FORMA_PAGAMENTO = Map.of("P", 0.0, "D", 0.03, "C", 0.05);

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository)
    {
        this.contaRepository = contaRepository;
    }

    public Conta criarConta(int numeroConta, double saldoInicial)
    {
        validarNovaConta(numeroConta, saldoInicial);

        Conta conta = new Conta(numeroConta, saldoInicial);

        return contaRepository.salvar(conta);
    }

    private void validarNovaConta(int numeroConta, double saldoInicial)
    {
        if (saldoInicial < 0)
        {
            throw new SaldoNegativoException("Uma conta não pode ser criada com saldo negativo.");
        }

        if (contaRepository.existeConta(numeroConta))
        {
            throw new ContaExistenteException("A conta de número: " + numeroConta + " já existe na base de dados.");
        }
    }

    public Conta realizarTransacao(int numeroConta, String formaPagamento, double valor)
    {
        validarFormatoTransacao(formaPagamento, valor);

        Conta conta = consultarConta(numeroConta);

        double taxa  = calcularTaxa(formaPagamento, valor);
        double total = valor + taxa;

        validarTransacao(conta, total);

        conta.debitar(total);

        return contaRepository.salvar(conta);
    }

    private static void validarFormatoTransacao(String formaPagamento, double valor)
    {
        if (valor <= 0)
        {
            throw new ValorTransacaoInvalidoException("O valor de qualquer transação deve ser maior que zero.");
        }

        if (!TAXA_POR_FORMA_PAGAMENTO.containsKey(formaPagamento))
        {
            String formasAceitas = String.join(", ", TAXA_POR_FORMA_PAGAMENTO.keySet());

            throw new FormaPagamentoInvalidaException("Formas aceitas: " + formasAceitas);
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

    private double calcularTaxa(String formaPagamento, double valor)
    {
        return valor * TAXA_POR_FORMA_PAGAMENTO.get(formaPagamento);
    }

    private static void validarTransacao(Conta conta, double total)
    {
        if (conta.getSaldo() < total)
        {
            throw new SaldoInsuficienteException("Saldo: " + conta.getSaldo() + " Valor: " + total);
        }
    }
}
