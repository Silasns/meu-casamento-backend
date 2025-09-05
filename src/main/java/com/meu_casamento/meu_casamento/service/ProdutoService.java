package com.meu_casamento.meu_casamento.service;

import com.meu_casamento.meu_casamento.domain.LinkLoja;
import com.meu_casamento.meu_casamento.domain.Produto;
import com.meu_casamento.meu_casamento.dto.AtualizarStatusRequest;
import com.meu_casamento.meu_casamento.dto.LinkLojaDTO;
import com.meu_casamento.meu_casamento.dto.ProdutoRequest;
import com.meu_casamento.meu_casamento.dto.ProdutoResponse;
import com.meu_casamento.meu_casamento.exception.RecursoNaoEncontradoException;
import com.meu_casamento.meu_casamento.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Order.asc;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional// não funcionou, ver para que serve(readOnly = true) ver o por que de this::toResponse
    public List<ProdutoResponse> listarTodos() {
        var sort = Sort.by(asc("statusReservado"), asc("titulo")); // false primeiro, depois true
        return produtoRepository.findAll(sort)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProdutoResponse buscarPorId(UUID id) {
        Produto p = produtoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));
        return toResponse(p);
    }

    @Transactional
    public ProdutoResponse criarProduto(ProdutoRequest request) {
        Produto p = new Produto();
        aplicar(p, request);
        Produto salvo = produtoRepository.save(p);
        return toResponse(salvo);
    }

    @Transactional
    public ProdutoResponse atulizarStatus(UUID id, AtualizarStatusRequest request) {
        Produto p = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));
        if(request.reservado() == null ) {
            throw new IllegalArgumentException("Campo 'reservado' é obrigatório");
        }
        p.setStatusReservado(request.reservado());
        return toResponse(p);
    }

    private ProdutoResponse toResponse(Produto produto) {
        List<LinkLojaDTO> links = produto.getLinkLojas().stream()
                .map(l -> new LinkLojaDTO(l.getNomeLoja(),
                        l.getLinkProduto()))
                .collect(Collectors.toList());
        return new ProdutoResponse(
                produto.getId(),
                produto.getTitulo(),
                produto.getDescricao(),
                produto.getValor(),
                produto.isStatusReservado(),
                !produto.isStatusReservado(),
                links,
                produto.getImageUrl(),
                produto.getImageAlt()
        );
    }

    private void aplicar(Produto produto, ProdutoRequest request) {
        if(request.titulo() != null) produto.setTitulo(request.titulo());
        if(request.descricao() != null) produto.setDescricao(request.descricao());
        if(request.valor() != null) produto.setValor(request.valor());
        if(request.statusReservado() != null) produto.setStatusReservado(request.statusReservado());
        if(request.imagenUrl() != null) produto.setImageUrl(request.imagenUrl());
        if(request.imagenAlt() != null) produto.setImageAlt(request.imagenAlt());
        if(request.linksLoja() != null) {
            List<LinkLoja> links = request.linksLoja().stream()
                    .map(d -> new LinkLoja(d.getNomeLoja(), d.getLinkProduto()))
                    .collect(Collectors.toList());
            produto.setLinkLojas(links);
        }
    }
}
