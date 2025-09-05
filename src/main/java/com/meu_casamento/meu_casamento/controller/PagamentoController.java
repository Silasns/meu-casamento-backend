package com.meu_casamento.meu_casamento.controller;

import com.meu_casamento.meu_casamento.dto.CheckoutInput;
import com.meu_casamento.meu_casamento.dto.LinkPagamentoReponse;
import com.meu_casamento.meu_casamento.dto.LinkPagamentoRequest;
import com.meu_casamento.meu_casamento.service.CheckoutService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/link-pagamento")
public class PagamentoController {

    private final CheckoutService service;
    public PagamentoController(CheckoutService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<LinkPagamentoReponse> criar(@RequestBody @Valid CheckoutInput checkoutRequest) {
        return ResponseEntity.ok(service.criarCheckout(checkoutRequest));
    }
}
