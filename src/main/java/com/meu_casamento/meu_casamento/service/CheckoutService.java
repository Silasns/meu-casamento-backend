package com.meu_casamento.meu_casamento.service;


import com.meu_casamento.meu_casamento.config.CheckoutProps;
import com.meu_casamento.meu_casamento.dto.CheckoutInput;
import com.meu_casamento.meu_casamento.dto.CheckoutPayload;
import com.meu_casamento.meu_casamento.dto.LinkPagamentoReponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CheckoutProps props;
    private final WebClient gatewayWebClient;

    @Value("${gateway.checkout-path:/v1/checkout}") private String path;

    public LinkPagamentoReponse criarCheckout(CheckoutInput in) {
        var payload = new CheckoutPayload(
                props.handle(),
                props.redirectUrl(),
                props.webhookUrl(),
                in.order_nsu(),
                in.customer(),
                in.items()
        );

        return gatewayWebClient.post()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(LinkPagamentoReponse.class)
                .block();
    }
}
