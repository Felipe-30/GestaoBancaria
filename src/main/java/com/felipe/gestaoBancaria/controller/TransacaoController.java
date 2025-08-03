package com.felipe.gestaoBancaria.controller;

import com.felipe.gestaoBancaria.dto.ContaDTO;
import com.felipe.gestaoBancaria.dto.TransacaoDTO;
import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ContaDTO realizarTransacao(@RequestBody TransacaoDTO transacaoDTO)
    {
        Conta conta = contaService.realizarTransacao(transacaoDTO.getNumeroConta(), transacaoDTO.getFormaPagamento(), transacaoDTO.getValor());

        return new ContaDTO(conta.getNumeroConta(), conta.getSaldo());
    }
}
