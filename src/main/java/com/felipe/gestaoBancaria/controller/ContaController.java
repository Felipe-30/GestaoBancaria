package com.felipe.gestaoBancaria.controller;

import com.felipe.gestaoBancaria.dto.ContaDTO;
import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conta")
public class ContaController
{
    private final ContaService contaService;

    @Autowired
    public ContaController(ContaService contaService)
    {
        this.contaService = contaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContaDTO criarConta(@RequestBody ContaDTO contaDTO)
    {
        Conta conta = contaService.criarConta(contaDTO.getNumeroConta(), contaDTO.getSaldo());

        return new ContaDTO(conta.getNumeroConta(), conta.getSaldo());
    }

    @GetMapping("/{numero}")
    public ContaDTO consultarConta(@PathVariable int numero)
    {
        Conta conta = contaService.consultarConta(numero);

        return new ContaDTO(conta.getNumeroConta(), conta.getSaldo());
    }
}
