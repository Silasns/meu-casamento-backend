package com.meu_casamento.meu_casamento.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {
    
    private UUID id;
    private String nome;
    private String telefone;
    private String email;
    private String mensagem;
    private LocalDateTime dataCadastro;
    private String meioReserva;
    private List<UUID> produtosReservados; // Lista de IDs dos produtos
}
