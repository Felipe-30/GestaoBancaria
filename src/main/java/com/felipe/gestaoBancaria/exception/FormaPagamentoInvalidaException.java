package com.felipe.gestaoBancaria.exception;

public class FormaPagamentoInvalidaException extends RuntimeException
{
    public FormaPagamentoInvalidaException(String mensagem)
    {
        super(mensagem);
    }
}
