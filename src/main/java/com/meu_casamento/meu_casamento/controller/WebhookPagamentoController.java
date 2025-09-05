package com.meu_casamento.meu_casamento.controller;

import com.meu_casamento.meu_casamento.dto.PagamentoWebhook;
import com.meu_casamento.meu_casamento.service.WebhookPagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks")
public class WebhookPagamentoController {

    private final WebhookPagamentoService service;
    public WebhookPagamentoController(WebhookPagamentoService service){
        this.service = service;
    }

    @PostMapping("/pagamento")
    public ResponseEntity<Void> receber(@RequestBody @Valid PagamentoWebhook payload) {
        service.processar(payload);
        return ResponseEntity.ok().build(); // responda rapido; gateway pode reenviar em caso de erro
    }
}
