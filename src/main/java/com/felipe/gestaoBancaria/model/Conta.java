package com.felipe.gestaoBancaria.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


@Entity
public class Conta
{
    @Id
    private int        numeroConta;
    private BigDecimal saldo;

    @OneToMany(mappedBy = "conta")
    private List<Transacao> transacoes;

    protected Conta() {}

    public Conta(int numeroConta, BigDecimal saldoInicial)
    {
        this.numeroConta = numeroConta;
        this.saldo       = saldoInicial;
    }

    public void debitar(BigDecimal valor)
    {
        this.saldo = this.saldo.subtract(valor).setScale(2, RoundingMode.HALF_UP);
    }

    public void creditar(BigDecimal valor)
    {
        this.saldo =  this.saldo.add(valor.setScale(2, RoundingMode.HALF_UP));
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

    public BigDecimal getSaldo()
    {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo)
    {
        this.saldo = saldo;
    }
}
