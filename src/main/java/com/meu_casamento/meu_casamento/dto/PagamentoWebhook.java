package com.meu_casamento.meu_casamento.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PagamentoWebhook(
        @JsonProperty("invoice_slug") String invoiceSlug,
        Integer amount,
        @JsonProperty("paid_amount") Integer paidAmount,
        Integer installments,
        @JsonProperty("capture_method") String captureMethod,
        @JsonProperty("transaction_nsu") String transactionNsu,
        @JsonProperty("order_nsu") String orderNsu,
        @JsonProperty("receipt_url") String receiptUrl,
        List<Object> items
) {}