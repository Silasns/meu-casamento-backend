package com.meu_casamento.meu_casamento.service;

import com.meu_casamento.meu_casamento.domain.Produto;
import com.meu_casamento.meu_casamento.dto.PagamentoWebhook;
import com.meu_casamento.meu_casamento.dto.WebhookResponse;
import com.meu_casamento.meu_casamento.exception.RecursoNaoEncontradoException;
import com.meu_casamento.meu_casamento.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class WebhookPagamentoService {

    private final ProdutoRepository produtoRepository;
    private final WebhookEventoService webhookEventoService;
    
    public WebhookPagamentoService(ProdutoRepository produtoRepository, WebhookEventoService webhookEventoService) { 
        this.produtoRepository = produtoRepository;
        this.webhookEventoService = webhookEventoService;
    }

    @Transactional
    public WebhookResponse processar(PagamentoWebhook webhook) {
        try {
            log.info("Processando webhook da InfinitePay: invoice_slug={}, order_nsu={}, amount={}, paid_amount={}", 
                    webhook.getInvoiceSlug(), webhook.getOrderNsu(), webhook.getAmount(), webhook.getPaidAmount());

            // Verificar se o pagamento foi aprovado
            if (!isPagamentoAprovado(webhook)) {
                log.info("Pagamento não foi aprovado. Ignorando webhook.");
                webhookEventoService.registrar("infinitepay", webhook, null, false, true, null);
                return WebhookResponse.sucesso();
            }

            // Extrair o ID do produto do order_nsu
            UUID produtoId = extrairProdutoId(webhook.getOrderNsu());
            
            // Buscar o produto
            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + produtoId));

            // Verificar se o produto já está reservado
            if (produto.isStatusReservado()) {
                log.warn("Produto {} já está reservado. Ignorando webhook.", produtoId);
                webhookEventoService.registrar("infinitepay", webhook, produtoId, true, true, null);
                return WebhookResponse.sucesso();
            }

            // Marcar produto como reservado
            produto.setStatusReservado(true);
            produtoRepository.save(produto);

            log.info("Produto {} marcado como reservado com sucesso via webhook da InfinitePay", produtoId);
            webhookEventoService.registrar("infinitepay", webhook, produtoId, true, true, null);
            
            return WebhookResponse.sucesso();

        } catch (IllegalArgumentException e) {
            log.error("Erro ao processar order_nsu: {}", webhook.getOrderNsu(), e);
            webhookEventoService.registrar("infinitepay", webhook, null, false, false, "Formato inválido do order_nsu");
            return WebhookResponse.erro("Formato inválido do order_nsu");
        } catch (RecursoNaoEncontradoException e) {
            log.error("Produto não encontrado: {}", e.getMessage());
            webhookEventoService.registrar("infinitepay", webhook, null, true, false, "Produto não encontrado");
            return WebhookResponse.erro("Produto não encontrado");
        } catch (Exception e) {
            log.error("Erro inesperado ao processar webhook", e);
            webhookEventoService.registrar("infinitepay", webhook, null, false, false, "Erro interno do servidor");
            return WebhookResponse.erro("Erro interno do servidor");
        }
    }

    private boolean isPagamentoAprovado(PagamentoWebhook webhook) {
        // Verificar se o valor pago é maior ou igual ao valor esperado
        Integer valorEsperado = webhook.getAmount();
        Integer valorPago = webhook.getPaidAmount();
        
        if (valorEsperado == null || valorPago == null) {
            return false;
        }
        
        return valorPago >= valorEsperado;
    }

    private UUID extrairProdutoId(String orderNsu) {
        // Assumindo que o order_nsu é o UUID do produto
        // Se você usar uma estratégia diferente, ajuste aqui
        return UUID.fromString(orderNsu);
    }
}
