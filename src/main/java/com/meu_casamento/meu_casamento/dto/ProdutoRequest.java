package com.meu_casamento.meu_casamento.dto;

import com.meu_casamento.meu_casamento.domain.LinkLoja;

import java.util.List;

public record ProdutoRequest(
        String titulo,
        String descricao,
        Integer valor,
        Boolean statusReservado,
        List<LinkLoja> linksLoja,
        String imagenUrl,
        String imagenAlt
) {
}
