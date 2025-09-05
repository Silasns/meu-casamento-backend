package com.meu_casamento.meu_casamento.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.util.List;

public record CheckoutInput(
        @JsonProperty("order_nsu") @NotBlank String order_nsu,
        @NotNull Customer customer,
        @NotEmpty List<Item> items
) {
    public record Customer(
            @NotBlank String name,
            @Email @NotBlank String email,
            @JsonProperty("phone_number") @NotBlank String phone_number
    ) {}
    public record Item(
            @Positive Integer quantity,
            @Positive Integer price,
            @NotBlank String description
    ) {}
}
