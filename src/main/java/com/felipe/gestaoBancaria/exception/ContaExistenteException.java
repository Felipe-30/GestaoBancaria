package com.felipe.gestaoBancaria.exception;

public class ContaExistenteException extends RuntimeException
{
    public ContaExistenteException(String mensagem)
    {
        super(mensagem);
    }
}