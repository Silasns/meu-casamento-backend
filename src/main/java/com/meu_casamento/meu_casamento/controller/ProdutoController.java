package com.meu_casamento.meu_casamento.controller;

import com.meu_casamento.meu_casamento.domain.Produto;
import com.meu_casamento.meu_casamento.dto.AtualizarStatusRequest;
import com.meu_casamento.meu_casamento.dto.ProdutoRequest;
import com.meu_casamento.meu_casamento.dto.ProdutoResponse;
import com.meu_casamento.meu_casamento.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private ProdutoService produtoService;
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<ProdutoResponse> listar(){
        return produtoService.listarTodos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse criar(@RequestBody ProdutoRequest produtoRequest){
        return produtoService.criarProduto(produtoRequest);
    }

    @PatchMapping("/{id}/status")
    public  ProdutoResponse atualizarStatus(@PathVariable UUID id, @RequestBody @Valid AtualizarStatusRequest request){
        return produtoService.atulizarStatus(id, request);
    }
}
