package com.meu_casamento.meu_casamento.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CheckoutPayload(
        String handle,
        @JsonProperty ("redirect_url") String redirectUrl,
        @JsonProperty("webhook_url") String webhookUrl,
        @JsonProperty("order_nsu") String orderNsu,
        CheckoutInput.Customer customer,
        List<CheckoutInput.Item> items
) {}
