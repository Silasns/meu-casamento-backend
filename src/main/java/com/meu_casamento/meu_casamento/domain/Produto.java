package com.meu_casamento.meu_casamento.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="produto")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String titulo;

    @Column(length = 2000)
    private String descricao;

    @NotNull
    private Integer valor;

    //Pedir para explicar essa parte de baixc
    @JsonProperty("statusReservado")
    @JsonAlias({"statusReservado"})
    @Column(name= "status_reservado", nullable = false)
    @Builder.Default
    private boolean statusReservado = false;

    //Pedir para explicar essa parte de baixc
    @ElementCollection
    @CollectionTable(name="produto_links", joinColumns = @JoinColumn(name = "produto_id" ))
    private List<LinkLoja> linkLojas = new ArrayList<>();

    private String imageUrl;
    private String imageAlt;

    //Pedir para explicar essa parte de baixc
    @Transient
    private boolean isDisponivel() {return !statusReservado;}
}
