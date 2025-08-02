package com.felipe.gestaoBancaria.repository;

import com.felipe.gestaoBancaria.model.Conta;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContaRepository
{
    private Map<Integer, Conta> contas = new ConcurrentHashMap<>();

    public Conta salvar(Conta conta)
    {
        contas.put(conta.getNumeroConta(), conta);
        return conta;
    }

    public Conta buscarContaPorNumero(int numeroConta)
    {
        return contas.get(numeroConta);
    }

    public boolean existeConta(int numeroConta)
    {
        return contas.containsKey(numeroConta);
    }
}
