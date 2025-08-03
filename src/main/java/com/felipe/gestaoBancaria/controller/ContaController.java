package com.felipe.gestaoBancaria.controller;

import com.felipe.gestaoBancaria.dto.ContaDTO;
import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ContaDTO criarConta(@Valid @RequestBody ContaDTO contaDTO)
    {
        Conta conta = contaService.criarConta(contaDTO.getNumeroConta(), contaDTO.getSaldo());

        return new ContaDTO(conta.getNumeroConta(), conta.getSaldo());
    }

    @GetMapping
    public ResponseEntity<ContaDTO> consultarConta(@RequestParam("numero_conta") Integer numeroConta)
    {
        try
        {
            Conta conta = contaService.consultarConta(numeroConta);

            ContaDTO contaDTO = new ContaDTO(conta.getNumeroConta(), conta.getSaldo());

            return ResponseEntity.ok(contaDTO);
        }

        catch (IllegalArgumentException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
