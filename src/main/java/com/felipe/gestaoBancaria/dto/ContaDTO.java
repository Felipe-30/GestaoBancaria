package com.felipe.gestaoBancaria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@JsonPropertyOrder({ "numero_conta", "saldo" })
public class ContaDTO
{
    @NotNull
    @JsonProperty("numero_conta")
    private Integer numeroConta;

    @NotNull
    @PositiveOrZero
    private Double saldo;


    public ContaDTO() {}

    public ContaDTO(Integer numeroConta, Double saldo)
    {
        this.numeroConta = numeroConta;
        this.saldo       = saldo;
    }

    // getters e setters
    public Integer getNumeroConta()
    {
        return numeroConta;
    }

    public void setNumeroConta(Integer numeroConta)
    {
        this.numeroConta = numeroConta;
    }

    public Double getSaldo()
    {
        return saldo;
    }

    public void setSaldo(Double saldo)
    {
        this.saldo = saldo;
    }
}
