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
        // Logs para console (garantem que apareçam no Dokploy)
        System.out.println("=== WEBHOOK INFINITE PAY RECEBIDO ===");
        System.out.println("Timestamp: " + java.time.LocalDateTime.now());
        System.out.println("Invoice Slug: " + payload.getInvoiceSlug());
        System.out.println("Valor: " + payload.getAmount());
        System.out.println("Items: " + payload.getItems());
        System.out.println("Payload completo: " + payload);
        System.out.println("=====================================");
        
        // Logs tradicionais também
        log.info("=== WEBHOOK INFINITE PAY RECEBIDO ===");
        log.info("Timestamp: {}", java.time.LocalDateTime.now());
        log.info("Invoice Slug: {}", payload.getInvoiceSlug());
        log.info("Valor: {}", payload.getAmount());
        log.info("Items: {}", payload.getItems());
        log.info("Payload completo: {}", payload);
        log.info("=====================================");
        
        try {
            System.out.println("Iniciando processamento do webhook...");
            log.info("Iniciando processamento do webhook...");
            
            WebhookResponse response = service.processar(payload);
            
            System.out.println("Processamento concluído. Sucesso: " + response.isSuccess());
            System.out.println("Mensagem: " + response.getMessage());
            log.info("Processamento concluído. Sucesso: {}", response.isSuccess());
            log.info("Mensagem: {}", response.getMessage());
            
            // Conforme documentação da InfinitePay:
            // - 200 OK para sucesso
            // - 400 Bad Request para erro (que fará o InfinitePay reenviar)
            if (response.isSuccess()) {
                System.out.println("Retornando 200 OK para Infinite Pay");
                log.info("Retornando 200 OK para Infinite Pay");
                return ResponseEntity.ok(response);
            } else {
                System.out.println("Retornando 400 Bad Request para Infinite Pay - erro no processamento");
                log.warn("Retornando 400 Bad Request para Infinite Pay - erro no processamento");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            System.out.println("=== ERRO INESPERADO NO WEBHOOK ===");
            System.out.println("Erro: " + e.getMessage());
            System.out.println("Payload que causou erro: " + payload);
            System.out.println("===================================");
            
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
        //log.info("Status: {}", payload.getStatus());
        log.info("Payload completo: {}", payload);
        log.info("========================");
        
        WebhookResponse response = service.processar(payload);
        log.info("Resultado do teste - Sucesso: {}, Mensagem: {}", response.isSuccess(), response.getMessage());
        return ResponseEntity.ok(response);
    }
}
