package com.felipe.gestaoBancaria.repository;

import com.felipe.gestaoBancaria.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Integer>
{
    Conta findByNumeroConta(int numeroConta);

    boolean existsByNumeroConta(int numeroConta);
}
