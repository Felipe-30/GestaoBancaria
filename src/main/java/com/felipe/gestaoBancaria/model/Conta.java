package com.felipe.gestaoBancaria.model;

public class Conta
{
    private int    numeroConta;
    private double saldo;

    public Conta(int numeroConta, double saldoInicial)
    {
        this.numeroConta = numeroConta;
        this.saldo       = saldoInicial;
    }

    public void debitar(double valor)
    {
        this.saldo -= valor;
    }

    public void creditar(double valor)
    {
        this.saldo += valor;
    }

    // Getters e setters
    public int getNumeroConta()
    {
        return numeroConta;
    }

    public void setNumeroConta(int numeroConta)
    {
        this.numeroConta = numeroConta;
    }

    public double getSaldo()
    {
        return saldo;
    }

    public void setSaldo(double saldo)
    {
        this.saldo = saldo;
    }
}
