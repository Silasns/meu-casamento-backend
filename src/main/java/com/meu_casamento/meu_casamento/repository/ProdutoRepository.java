package com.meu_casamento.meu_casamento.repository;

import com.meu_casamento.meu_casamento.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
}
