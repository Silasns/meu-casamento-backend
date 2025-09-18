package com.meu_casamento.meu_casamento.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meu_casamento.meu_casamento.domain.WebhookEvento;
import com.meu_casamento.meu_casamento.dto.PagamentoWebhook;
import com.meu_casamento.meu_casamento.repository.WebhookEventoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebhookEventoService {

    private final WebhookEventoRepository repository;
    private final ObjectMapper objectMapper;

    @Transactional
    public WebhookEvento registrar(String fonte,
                                   PagamentoWebhook payload,
                                   UUID produtoId,
                                   boolean aprovado,
                                   boolean sucessoProcessamento,
                                   String mensagemErro) {
        WebhookEvento evento = WebhookEvento.builder()
                .fonte(fonte)
                .invoiceSlug(payload.getInvoiceSlug())
                .orderNsu(payload.getOrderNsu())
                .produtoId(produtoId)
                .amount(payload.getAmount())
                .paidAmount(payload.getPaidAmount())
                .captureMethod(payload.getCaptureMethod())
                .transactionNsu(payload.getTransactionNsu())
                .receiptUrl(payload.getReceiptUrl())
                .itemsJson(payload.getItems())
                .payloadJson(payload)
                .aprovado(aprovado)
                .sucessoProcessamento(sucessoProcessamento)
                .mensagemErro(mensagemErro)
                .build();

        return repository.save(evento);
    }
}


