package com.meu_casamento.meu_casamento.service;

import com.meu_casamento.meu_casamento.domain.Produto;
import com.meu_casamento.meu_casamento.domain.Usuario;
import com.meu_casamento.meu_casamento.dto.AtualizarStatusRequest;
import com.meu_casamento.meu_casamento.dto.UsuarioRequest;
import com.meu_casamento.meu_casamento.dto.UsuarioResponse;
import com.meu_casamento.meu_casamento.exception.RecursoNaoEncontradoException;
import com.meu_casamento.meu_casamento.repository.ProdutoRepository;
import com.meu_casamento.meu_casamento.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final ProdutoService produtoService;

    public UsuarioService(UsuarioRepository usuarioRepository, ProdutoRepository produtoRepository, ProdutoService produtoService) {
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
        this.produtoService = produtoService;
    }

    @Transactional
    public UsuarioResponse salvarReserva(UsuarioRequest request) {
        // Verificar se o produto existe
        Produto produto = produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + request.getProdutoId()));

        // Verificar se o produto está disponível (não reservado)
        if (produto.isStatusReservado()) {
            throw new IllegalArgumentException("Este produto já foi reservado por outro usuário");
        }

        // Buscar usuário existente
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElse(null);
        
        if (usuario != null) {
            // Verificar se o usuário já tem este produto reservado
            if (usuario.getProdutosReservados().contains(request.getProdutoId())) {
                throw new IllegalArgumentException("Este produto já está na sua lista de reservas");
            }
            // Adicionar o produto à lista do usuário existente
            usuario.getProdutosReservados().add(request.getProdutoId());
        } else {
            // Criar novo usuário com o produto
            usuario = criarNovoUsuario(request);
        }

        // Salvar o usuário atualizado
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // Atualizar o status do produto como reservado
        produtoService.atulizarStatus(request.getProdutoId(), new AtualizarStatusRequest(true));

        // Retornar o usuário com todos os produtos
        return toResponse(usuarioSalvo);
    }

    private Usuario criarNovoUsuario(UsuarioRequest request) {
        Usuario novoUsuario = Usuario.builder()
                .nome(request.getNome())
                .telefone(request.getTelefone())
                .email(request.getEmail())
                .mensagem(request.getMensagem())
                .meioReserva(request.getMeioReserva())
                .produtosReservados(List.of(request.getProdutoId())) // Inicia com o primeiro produto
                .build();
        return usuarioRepository.save(novoUsuario);
    }

    @Transactional
    public UsuarioResponse buscarPorId(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + usuarioId));

        return toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + email));

        return toResponse(usuario);
    }

    @Transactional
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .telefone(usuario.getTelefone())
                .email(usuario.getEmail())
                .mensagem(usuario.getMensagem())
                .dataCadastro(usuario.getDataCadastro())
                .meioReserva(usuario.getMeioReserva().name())
                .produtosReservados(usuario.getProdutosReservados())
                .build();
    }
}
