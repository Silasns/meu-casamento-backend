package com.meu_casamento.meu_casamento.service;

import com.meu_casamento.meu_casamento.dto.PagamentoWebhook;
import com.meu_casamento.meu_casamento.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class WebhookPagamentoService {

    private final ProdutoRepository produtoRepository;
    public WebhookPagamentoService(ProdutoRepository produtoRepository){ this.produtoRepository = produtoRepository;}

    @Transactional
    public void processar(PagamentoWebhook p) {
        boolean pago = Optional.ofNullable(p.paidAmount()).orElse(0)
                >= Optional.ofNullable(p.amount()).orElse(Integer.MAX_VALUE);

        if (!pago) return;

        // Estratégia simples: order_nsu == UUID do produto
        try {
            UUID produtoId = UUID.fromString(p.orderNsu());
            produtoRepository.findById(produtoId).ifPresent(prod -> {
                prod.setStatusReservado(true); // JPA dirty checking salva no commit
            });
        } catch (IllegalArgumentException e) {
            // se não for UUID, adapte: mapear order_nsu -> produto_id em outra tabela
        }
    }
}
