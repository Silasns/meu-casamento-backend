package com.meu_casamento.meu_casamento.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoWebhook {
    
    @JsonProperty("invoice_slug")
    private String invoiceSlug;
    
    private Integer amount;
    
    @JsonProperty("paid_amount")
    private Integer paidAmount;
    
    private Integer installments;
    
    @JsonProperty("capture_method")
    private String captureMethod; // "credit_card" ou "pix"
    
    @JsonProperty("transaction_nsu")
    private String transactionNsu;
    
    @JsonProperty("order_nsu")
    private String orderNsu;
    
    @JsonProperty("receipt_url")
    private String receiptUrl;
    
    private List<ItemWebhook> items;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemWebhook {
        private Integer quantity;
        private Integer price;
        private String description;
    }
}