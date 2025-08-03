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
    @JoinColumn(name = "numeroConta")
    private Conta conta;

    private String     formaPagamento;
    private BigDecimal valor;
    private BigDecimal taxa;

    @Column(nullable = false)
    private LocalDateTime dataTransacao;

    public Transacao()
    {
        this.dataTransacao = LocalDateTime.now();
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
