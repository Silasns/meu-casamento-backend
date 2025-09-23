package com.meu_casamento.meu_casamento.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tentativa_pagamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TentativaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false)
    private String telefone;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @Column(columnDefinition = "text")
    private String mensagem;

    @NotNull
    @Column(name = "produto_id", nullable = false)
    private UUID produtoId;

    @Builder.Default
    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Builder.Default
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPagamento status = StatusPagamento.PENDENTE;

    @Column(name = "pago_em")
    private LocalDateTime pagoEm;

    @Column(name = "order_nsu")
    private String orderNsu;

    public enum StatusPagamento {
        PENDENTE, PAGO, CANCELADO
    }
}
