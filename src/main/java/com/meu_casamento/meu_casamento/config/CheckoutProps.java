package com.meu_casamento.meu_casamento.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "checkout")
public class CheckoutProps {
    private String handle;
    private String redirectUrl;
    private String webhookUrl;

    public String handle() { return handle; }
    public String redirectUrl() { return redirectUrl; }
    public String webhookUrl() { return webhookUrl; }

    public void setHandle(String v) { this.handle = v; }
    public void setRedirectUrl(String v) { this.redirectUrl = v; }
    public void setWebhookUrl(String v) { this.webhookUrl = v; }
}