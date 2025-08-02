package com.felipe.gestaoBancaria.service;

import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.repository.ContaRepository;

public class ContaService
{
    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository)
    {
        this.contaRepository = contaRepository;
    }

    public Conta criarConta(int numeroConta, double saldoInicial) throws IllegalArgumentException
    {
        validarNovaConta(numeroConta, saldoInicial);

        Conta conta = new Conta(numeroConta, saldoInicial);

        return contaRepository.salvar(conta);
    }

    private void validarNovaConta(int numeroConta, double saldoInicial)
    {
        if (saldoInicial < 0)
        {
            throw new IllegalArgumentException("Saldo da Conta não pode ser negativo.");
        }

        if (contaRepository.existeConta(numeroConta))
        {
            throw new IllegalArgumentException("Conta já existe.");
        }
    }

    public Conta realizarTransacao(int numeroConta, String formaPagamento, double valor) throws IllegalArgumentException
    {
        if (valor <= 0)
        {
            throw new IllegalArgumentException("O valor da transação deve ser positivo.");
        }

        Conta conta = consultarConta(numeroConta);

        double taxa  = calcularTaxa(formaPagamento, valor);
        double total = valor + taxa;

        validarTransacao(conta, total);

        conta.debitar(total);

        return contaRepository.salvar(conta);
    }

    public Conta consultarConta(int numeroConta) throws IllegalArgumentException
    {
        Conta conta = contaRepository.buscarContaPorNumero(numeroConta);

        if (conta == null)
        {
            throw new IllegalArgumentException("Conta não encontrada.");
        }

        return conta;
    }

    private double calcularTaxa(String formaPagamento, double valor)
    {
        return switch (formaPagamento)
        {
            case "P" -> 0;
            case "D" -> valor * 0.03;
            case "C" -> valor * 0.05;

            default -> throw new IllegalArgumentException("Forma de pagamento inválida.");
        };
    }

    private static void validarTransacao(Conta conta, double total)
    {
        if (conta.getSaldo() < total)
        {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
    }
}
