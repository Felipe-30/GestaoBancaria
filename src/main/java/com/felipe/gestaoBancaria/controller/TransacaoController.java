package com.felipe.gestaoBancaria.controller;

import com.felipe.gestaoBancaria.dto.ContaDTO;
import com.felipe.gestaoBancaria.dto.TransacaoDTO;
import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacao")
public class TransacaoController
{
    private final ContaService contaService;

    @Autowired
    public TransacaoController(ContaService contaService)
    {
        this.contaService = contaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ContaDTO> realizarTransacao(@Valid @RequestBody TransacaoDTO transacaoDTO)
    {
        Conta conta = contaService.realizarTransacao(transacaoDTO.getNumeroConta(), transacaoDTO.getFormaPagamento(), transacaoDTO.getValor());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ContaDTO(conta.getNumeroConta(), conta.getSaldo()));
    }
}
