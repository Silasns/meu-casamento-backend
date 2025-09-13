package com.meu_casamento.meu_casamento.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookResponse {
    
    private boolean success;
    private String message;
    
    public static WebhookResponse sucesso() {
        return WebhookResponse.builder()
                .success(true)
                .message(null)
                .build();
    }
    
    public static WebhookResponse erro(String mensagem) {
        return WebhookResponse.builder()
                .success(false)
                .message(mensagem)
                .build();
    }
}
