package com.felipe.gestaoBancaria.controller;

import com.felipe.gestaoBancaria.dto.ContaDTO;
import com.felipe.gestaoBancaria.dto.TransacaoDTO;
import com.felipe.gestaoBancaria.dto.TransacaoResponseDTO;
import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transacao")
public class TransacaoController 
{
    private final TransacaoService transacaoService;

    @Autowired
    public TransacaoController(TransacaoService transacaoService) 
    {
        this.transacaoService = transacaoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ContaDTO> realizarTransacao(@Valid @RequestBody TransacaoDTO transacaoDTO)
    {
        Conta conta = transacaoService.realizarTransacao(transacaoDTO.getNumeroConta(), transacaoDTO.getFormaPagamento(), transacaoDTO.getValor());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ContaDTO(conta.getNumeroConta(), conta.getSaldo()));
    }
    
    @GetMapping("/todas")
    public ResponseEntity<List<TransacaoResponseDTO>> listarTransacoesPorConta(@RequestParam("numero_conta") int numeroConta) 
    {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarTransacoesPorConta(numeroConta);

        return transacoes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transacoes);
    }

    @GetMapping("/forma-pagamento")
    public ResponseEntity<List<TransacaoResponseDTO>> listarTransacoesPorFormaPagamento(
            @RequestParam("numero_conta") int numeroConta,
            @RequestParam("forma") String forma)
    {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarTransacoesPorFormaPagamento(numeroConta, forma);

        return transacoes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transacoes);
    }

    @GetMapping("/valor-minimo")
    public ResponseEntity<List<TransacaoResponseDTO>> listarTransacoesPorValorMaiorQue(
            @RequestParam("numero_conta") int numeroConta,
            @RequestParam("valor") BigDecimal valor)
    {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarTransacoesPorValorMaiorQue(numeroConta, valor);

        return transacoes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transacoes);
    }

    @GetMapping("/ordenadas")
    public ResponseEntity<List<TransacaoResponseDTO>> listarTransacoesOrdenadasPorData(@RequestParam("numero_conta") int numeroConta)
    {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarTransacoesOrdenadasPorData(numeroConta);

        return transacoes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transacoes);
    }

    @GetMapping("/intervalo")
    public ResponseEntity<List<TransacaoResponseDTO>> listarTransacoesPorIntervaloDatas(
            @RequestParam("numero_conta") int numeroConta,
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim)
    {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarTransacoesPorIntervaloDeDatas(numeroConta, inicio, fim);

        return transacoes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transacoes);
    }
}
