package com.meu_casamento.meu_casamento.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "webhook_evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Builder.Default
    @Column(name = "recebido_em", nullable = false)
    private LocalDateTime recebidoEm = LocalDateTime.now();

    @Column(name = "fonte", nullable = false)
    private String fonte;

    @Column(name = "invoice_slug")
    private String invoiceSlug;

    @Column(name = "order_nsu")
    private String orderNsu;

    @Column(name = "produto_id")
    private UUID produtoId;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "paid_amount")
    private Integer paidAmount;

    @Column(name = "capture_method")
    private String captureMethod;

    @Column(name = "transaction_nsu")
    private String transactionNsu;

    @Column(name = "receipt_url", length = 1000)
    private String receiptUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "items_json", columnDefinition = "jsonb")
    private String itemsJson;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload_json", columnDefinition = "jsonb", nullable = false)
    private String payloadJson;

    @Column(name = "aprovado", nullable = false)
    private boolean aprovado;

    @Column(name = "sucesso_processamento", nullable = false)
    private boolean sucessoProcessamento;

    @Column(name = "mensagem_erro", columnDefinition = "text")
    private String mensagemErro;
}


