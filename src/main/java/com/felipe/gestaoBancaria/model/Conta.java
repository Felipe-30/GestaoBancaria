package com.felipe.gestaoBancaria.model;

import com.felipe.gestaoBancaria.utils.BigDecimalUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
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
        this.saldo = BigDecimalUtils.arredondar(this.saldo.subtract(valor));
    }

    public void creditar(BigDecimal valor)
    {
        this.saldo = BigDecimalUtils.arredondar(this.saldo.add(valor));
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
