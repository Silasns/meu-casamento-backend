package com.meu_casamento.meu_casamento.dto;



import java.util.List;
import java.util.UUID;

public record ProdutoResponse(
        UUID id,
        String titulo,
        String descricao,
        Integer valor,
        Boolean statusReservado,
        Boolean disponivel,
        List<LinkLojaDTO> linkLojas,
        String imageUrl,
        String imageAlt
) {
}
