package com.meu_casamento.meu_casamento.repository;

import com.meu_casamento.meu_casamento.domain.TentativaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TentativaPagamentoRepository extends JpaRepository<TentativaPagamento, UUID> {
    
    Optional<TentativaPagamento> findByEmailAndProdutoIdAndStatus(String email, UUID produtoId, TentativaPagamento.StatusPagamento status);
    
    List<TentativaPagamento> findByProdutoIdAndStatus(UUID produtoId, TentativaPagamento.StatusPagamento status);
    
    Optional<TentativaPagamento> findByOrderNsu(String orderNsu);
}
