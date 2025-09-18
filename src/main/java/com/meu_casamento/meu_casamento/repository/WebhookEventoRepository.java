package com.meu_casamento.meu_casamento.repository;

import com.meu_casamento.meu_casamento.domain.WebhookEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WebhookEventoRepository extends JpaRepository<WebhookEvento, UUID> {
}


