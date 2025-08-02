package com.felipe.gestaoBancaria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@JsonPropertyOrder({ "numero_conta", "saldo" })
public class ContaDTO
{
    @NotNull
    @JsonProperty("numero_conta")
    private Integer numeroConta;

    @NotNull
    @PositiveOrZero
    private BigDecimal saldo;


    public ContaDTO() {}

    public ContaDTO(Integer numeroConta, BigDecimal saldo)
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

    public BigDecimal getSaldo()
    {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo)
    {
        this.saldo = saldo;
    }
}
