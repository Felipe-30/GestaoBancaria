package com.felipe.gestaoBancaria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;


public class TransacaoDTO
{
    @NotNull
    @JsonProperty("forma_pagamento")
    private String formaPagamento;

    @NotNull
    @Positive
    @JsonProperty("numero_conta")
    private Integer numeroConta;

    @NotNull
    @Positive
    private BigDecimal valor;


    public TransacaoDTO() {}

    public TransacaoDTO(Integer numeroConta, String formaPagamento, BigDecimal valor)
    {
        this.formaPagamento = formaPagamento;
        this.numeroConta    = numeroConta;
        this.valor          = valor;
    }


    // getters e setters
    public String getFormaPagamento()
    {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento)
    {
        this.formaPagamento = formaPagamento;
    }

    public Integer getNumeroConta()
    {
        return numeroConta;
    }

    public void setNumeroConta(Integer numeroConta)
    {
        this.numeroConta = numeroConta;
    }

    public BigDecimal getValor()
    {
        return valor;
    }

    public void setValor(BigDecimal valor)
    {
        this.valor = valor;
    }
}

