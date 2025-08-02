package com.felipe.gestaoBancaria.exception;

public class SaldoInsuficienteException extends RuntimeException
{
    public SaldoInsuficienteException(String mensagem)
    {
        super(mensagem);
    }
}
