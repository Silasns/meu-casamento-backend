package com.meu_casamento.meu_casamento.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    
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
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(columnDefinition = "text")
    private String mensagem;
    
    @Builder.Default
    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "meio_reserva", nullable = false)
    private MeioReserva meioReserva;
    
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "produtos_reservados", columnDefinition = "uuid[]")
    @Builder.Default
    private List<UUID> produtosReservados = new ArrayList<>();
    
    public enum MeioReserva {
        lojas, card
    }
}
