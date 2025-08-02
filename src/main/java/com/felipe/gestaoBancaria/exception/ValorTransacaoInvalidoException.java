package com.felipe.gestaoBancaria.exception;

public class ValorTransacaoInvalidoException extends RuntimeException
{
    public ValorTransacaoInvalidoException(String mensagem)
    {
        super(mensagem);
    }
}
