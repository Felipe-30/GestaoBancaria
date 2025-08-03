package com.felipe.gestaoBancaria.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class Conta
{
    @Id
    private int    numeroConta;
    private double saldo;

    protected Conta()
    {

    }

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
