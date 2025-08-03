package com.felipe.gestaoBancaria.service;

import com.felipe.gestaoBancaria.exception.ContaExistenteException;
import com.felipe.gestaoBancaria.exception.ContaNaoEncontradaException;
import com.felipe.gestaoBancaria.exception.SaldoNegativoException;
import com.felipe.gestaoBancaria.model.Conta;
import com.felipe.gestaoBancaria.repository.ContaRepository;
import com.felipe.gestaoBancaria.utils.BigDecimalUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ContaService
{
    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository)
    {
        this.contaRepository = contaRepository;
    }

    public Conta criarConta(int numeroConta, BigDecimal saldoInicial)
    {
        validarNovaConta(numeroConta, saldoInicial);

        Conta conta = new Conta(numeroConta, BigDecimalUtils.arredondar(saldoInicial));

        return contaRepository.save(conta);
    }

    private void validarNovaConta(int numeroConta, BigDecimal saldoInicial)
    {
        validarSaldoInicial(saldoInicial);

        if (contaRepository.existsByNumeroConta(numeroConta))
        {
            throw new ContaExistenteException("A conta de número: " + numeroConta + " já existe na base de dados.");
        }
    }

    private static void validarSaldoInicial(BigDecimal saldoInicial)
    {
        if (saldoInicial == null)
        {
            throw new IllegalArgumentException("Saldo inicial não pode ser nulo.");
        }

        if (saldoInicial.scale() > 2)
        {
            throw new IllegalArgumentException("Saldo inicial deve ter no máximo 2 casas decimais.");
        }

        if (saldoInicial.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new SaldoNegativoException("Uma conta não pode ser criada com saldo negativo.");
        }
    }

    public Conta consultarConta(int numeroConta)
    {
        return contaRepository.findById(numeroConta).orElseThrow(() ->
                new ContaNaoEncontradaException("A conta de número: " + numeroConta + " não foi encontrada na base de dados."));
    }
}
