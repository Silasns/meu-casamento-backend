package com.meu_casamento.meu_casamento.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkLoja {
    @NotBlank
    private String nomeLoja;

    @NotBlank
    private String linkProduto;
}
