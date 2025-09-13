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
        log.info("Recebendo webhook da InfinitePay: {}", payload.getInvoiceSlug());
        
        try {
            WebhookResponse response = service.processar(payload);
            
            // Conforme documentação da InfinitePay:
            // - 200 OK para sucesso
            // - 400 Bad Request para erro (que fará o InfinitePay reenviar)
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("Erro inesperado ao processar webhook da InfinitePay", e);
            WebhookResponse errorResponse = WebhookResponse.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    // Endpoint para teste local (opcional)
    @PostMapping("/teste")
    public ResponseEntity<WebhookResponse> testeWebhook(@RequestBody @Valid PagamentoWebhook payload) {
        log.info("Teste de webhook recebido: {}", payload.getInvoiceSlug());
        WebhookResponse response = service.processar(payload);
        return ResponseEntity.ok(response);
    }
}
