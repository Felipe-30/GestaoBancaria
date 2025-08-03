package com.felipe.gestaoBancaria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransacaoResponseDTO
{
    @JsonProperty("numero_conta")
    private Integer numeroConta;

    @JsonProperty("forma_pagamento")
    private String formaPagamento;

    private BigDecimal valor;

    private BigDecimal taxa;

    @JsonProperty("data_transacao")
    private LocalDateTime dataTransacao;

    public TransacaoResponseDTO() {}

    public TransacaoResponseDTO(Integer numeroConta, String formaPagamento, BigDecimal valor, BigDecimal taxa, LocalDateTime dataTransacao)
    {
        this.taxa           = taxa;
        this.valor          = valor;
        this.dataTransacao  = dataTransacao;
        this.formaPagamento = formaPagamento;
        this.numeroConta    = numeroConta;
    }

    // Getters e setters
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

    public void setDataTransacao(LocalDateTime dataTransacao)
    {
        this.dataTransacao = dataTransacao;
    }

    public Integer getNumeroConta()
    {
        return numeroConta;
    }

    public void setNumeroConta(Integer numeroConta)
    {
        this.numeroConta = numeroConta;
    }
}
