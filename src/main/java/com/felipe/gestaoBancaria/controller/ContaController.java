package com.felipe.gestaoBancaria.controller;

import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contas")
public class ContaController
{
    private final ContaService contaService;

    @Autowired
    public ContaController(ContaService contaService)
    {
        this.contaService = contaService;
    }

    @PostMapping
    public Conta criarConta(@RequestParam int numero, @RequestParam double saldoInicial)
    {
        return contaService.criarConta(numero, saldoInicial);
    }

    @GetMapping("/{numero}")
    public Conta consultarConta(@PathVariable int numero)
    {
        return contaService.consultarConta(numero);
    }

    @PostMapping("/{numero}/transacoes")
    public Conta realizarTransacao(@PathVariable int numero, @RequestParam String formaPagamento, @RequestParam double valor)
    {
        return contaService.realizarTransacao(numero, formaPagamento, valor);
    }
}
