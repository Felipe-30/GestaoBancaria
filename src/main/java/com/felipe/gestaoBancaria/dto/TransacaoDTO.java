package com.felipe.gestaoBancaria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public class TransacaoDTO
{
    @NotNull
    @JsonProperty("forma_pagamento")
    private String formaPagamento;

    @NotNull
    @JsonProperty("numero_conta")
    private Integer numeroConta;

    @NotNull
    @Positive
    private Double valor;


    public TransacaoDTO() {}

    public TransacaoDTO(Integer numeroConta, String formaPagamento, Double valor)
    {
        this.formaPagamento = formaPagamento;
        this.numeroConta = numeroConta;
        this.valor = valor;
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

    public Double getValor()
    {
        return valor;
    }

    public void setValor(Double valor)
    {
        this.valor = valor;
    }
}

