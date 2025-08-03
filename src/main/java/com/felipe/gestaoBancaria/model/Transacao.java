package com.felipe.gestaoBancaria.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transacao
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conta_numero_conta", nullable = false)
    private Conta conta;

    @Column(name = "forma_pagamento", length = 1, nullable = false)
    private String formaPagamento;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private BigDecimal taxa;

    @Column(nullable = false)
    private LocalDateTime dataTransacao;

    public Transacao()
    {
        this.dataTransacao = LocalDateTime.now();
    }

    public Transacao(Conta conta, String formaPagamento, BigDecimal valor, BigDecimal taxa, LocalDateTime dataTransacao)
    {
        this.taxa           = taxa;
        this.valor          = valor;
        this.conta          = conta;
        this.formaPagamento = formaPagamento;
        this.dataTransacao  = dataTransacao;
    }


    // Getters e setters
    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public String getFormaPagamento()
    {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento)
    {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getValor()
    {
        return valor;
    }

    public void setValor(BigDecimal valor)
    {
        this.valor = valor;
    }

    public Conta getConta()
    {
        return conta;
    }

    public void setConta(Conta conta)
    {
        this.conta = conta;
    }

    public BigDecimal getTaxa()
    {
        return taxa;
    }

    public void setTaxa(BigDecimal taxa)
    {
        this.taxa = taxa;
    }

    public LocalDateTime getDataTransacao()
    {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDateTime dataHora)
    {
        this.dataTransacao = dataHora;
    }
}
