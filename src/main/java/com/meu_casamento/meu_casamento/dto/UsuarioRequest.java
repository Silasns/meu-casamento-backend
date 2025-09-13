package com.meu_casamento.meu_casamento.dto;

import com.meu_casamento.meu_casamento.domain.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter um formato válido")
    private String email;
    
    private String mensagem;
    
    @NotNull(message = "ID do produto é obrigatório")
    private UUID produtoId;
    
    @NotNull(message = "Meio de reserva é obrigatório")
    private Usuario.MeioReserva meioReserva;
}
