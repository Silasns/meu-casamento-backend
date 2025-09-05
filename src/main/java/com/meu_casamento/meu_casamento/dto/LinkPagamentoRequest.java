package com.meu_casamento.meu_casamento.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.util.List;

public record LinkPagamentoRequest(
        @NotBlank String handle,
        @JsonProperty("redirect_url") @NotBlank String redirectUrl,
        @JsonProperty("order_nsu") @NotBlank String orderNsu,
        @NotNull Customer customer,
        @NotEmpty List<Item> items
) {
    public record Customer(
            @NotBlank String name,
            @Email @NotBlank String email,
            @JsonProperty("phone_number") @NotBlank String phoneNumber
    ) {}

    public record Item(
            @Positive Integer quantity,
            @Positive Integer price,
            @NotBlank String description
    ) {}
}
