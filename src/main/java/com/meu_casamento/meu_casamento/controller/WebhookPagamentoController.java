package com.meu_casamento.meu_casamento.controller;

import com.meu_casamento.meu_casamento.dto.PagamentoWebhook;
import com.meu_casamento.meu_casamento.dto.WebhookResponse;
import com.meu_casamento.meu_casamento.service.WebhookPagamentoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/webhooks")
public class WebhookPagamentoController {

    private final WebhookPagamentoService service;
    
    public WebhookPagamentoController(WebhookPagamentoService service) {
        this.service = service;
    }

    @PostMapping("/infinitepay")
    public ResponseEntity<WebhookResponse> receberWebhookInfinitePay(@RequestBody @Valid PagamentoWebhook payload) {
        log.info("=== WEBHOOK INFINITE PAY RECEBIDO ===");
        log.info("Timestamp: {}", java.time.LocalDateTime.now());
        log.info("Invoice Slug: {}", payload.getInvoiceSlug());
        log.info("Status: {}", payload.getStatus());
        log.info("Valor: {}", payload.getAmount());
        log.info("Moeda: {}", payload.getCurrency());
        log.info("Cliente: {}", payload.getCustomer());
        log.info("Items: {}", payload.getItems());
        log.info("Payload completo: {}", payload);
        log.info("=====================================");
        
        try {
            log.info("Iniciando processamento do webhook...");
            WebhookResponse response = service.processar(payload);
            log.info("Processamento concluído. Sucesso: {}", response.isSuccess());
            log.info("Mensagem: {}", response.getMessage());
            
            // Conforme documentação da InfinitePay:
            // - 200 OK para sucesso
            // - 400 Bad Request para erro (que fará o InfinitePay reenviar)
            if (response.isSuccess()) {
                log.info("Retornando 200 OK para Infinite Pay");
                return ResponseEntity.ok(response);
            } else {
                log.warn("Retornando 400 Bad Request para Infinite Pay - erro no processamento");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("=== ERRO INESPERADO NO WEBHOOK ===");
            log.error("Erro ao processar webhook da InfinitePay", e);
            log.error("Payload que causou erro: {}", payload);
            log.error("===================================");
            WebhookResponse errorResponse = WebhookResponse.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    // Endpoint para teste local (opcional)
    @PostMapping("/teste")
    public ResponseEntity<WebhookResponse> testeWebhook(@RequestBody @Valid PagamentoWebhook payload) {
        log.info("=== TESTE DE WEBHOOK ===");
        log.info("Timestamp: {}", java.time.LocalDateTime.now());
        log.info("Invoice Slug: {}", payload.getInvoiceSlug());
        log.info("Status: {}", payload.getStatus());
        log.info("Payload completo: {}", payload);
        log.info("========================");
        
        WebhookResponse response = service.processar(payload);
        log.info("Resultado do teste - Sucesso: {}, Mensagem: {}", response.isSuccess(), response.getMessage());
        return ResponseEntity.ok(response);
    }
}
