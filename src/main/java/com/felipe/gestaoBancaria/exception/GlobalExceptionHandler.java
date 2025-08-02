package com.felipe.gestaoBancaria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(ContaExistenteException.class)
    public ResponseEntity<Map<String, Object>> handleContaExistente(ContaExistenteException ex)
    {
        return construirRespostaErro(HttpStatus.CONFLICT, "Conta já existe.", ex);
    }

    @ExceptionHandler(SaldoNegativoException.class)
    public ResponseEntity<Map<String, Object>> handleSaldoNegativo(SaldoNegativoException ex)
    {
        return construirRespostaErro(HttpStatus.UNPROCESSABLE_ENTITY, "Conta com saldo negativo.", ex);
    }

    @ExceptionHandler(ValorTransacaoInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handleValorTransacaoInvalido(ValorTransacaoInvalidoException ex)
    {
        return construirRespostaErro(HttpStatus.UNPROCESSABLE_ENTITY, "Valor da transação inválido.", ex);
    }

    @ExceptionHandler(FormaPagamentoInvalidaException.class)
    public ResponseEntity<Map<String, Object>> handleFormaPagamentoInvalida(FormaPagamentoInvalidaException ex)
    {
        return construirRespostaErro(HttpStatus.UNPROCESSABLE_ENTITY, "Forma de pagamento inválida.", ex);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<Map<String, Object>> handleSaldoInsuficiente(SaldoInsuficienteException ex)
    {
        return construirRespostaErro(HttpStatus.UNPROCESSABLE_ENTITY, "Saldo insuficiente.", ex);
    }

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> handleContaNaoEncontrada(ContaNaoEncontradaException ex)
    {
        return construirRespostaErro(HttpStatus.NOT_FOUND, "Conta não encontrada.", ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex)
    {
        return construirRespostaErro(HttpStatus.BAD_REQUEST, "Argumento inválido.", ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex)
    {
        return construirRespostaErro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor.", ex);
    }

    private ResponseEntity<Map<String, Object>> construirRespostaErro(HttpStatus status, String tituloErro, Exception ex)
    {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status"   , status.value());
        body.put("error"    , tituloErro);
        body.put("message"  , ex.getMessage());

        return new ResponseEntity<>(body, status);
    }
}
