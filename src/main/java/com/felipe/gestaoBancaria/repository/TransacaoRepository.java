package com.felipe.gestaoBancaria.repository;

import com.felipe.gestaoBancaria.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long>
{
    List<Transacao> findByContaNumeroConta(int numeroConta);

    List<Transacao> findByContaNumeroContaAndFormaPagamento(int numeroConta, String formaPagamento);

    List<Transacao> findByContaNumeroContaAndValorGreaterThan(int numeroConta, java.math.BigDecimal valor);

    List<Transacao> findByContaNumeroContaOrderByDataTransacaoDesc(int numeroConta);

    List<Transacao> findByContaNumeroContaAndDataTransacaoBetween(int numeroConta, LocalDateTime inicio, LocalDateTime fim);
}
