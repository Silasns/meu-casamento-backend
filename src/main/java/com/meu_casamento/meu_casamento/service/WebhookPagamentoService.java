package com.meu_casamento.meu_casamento.service;

import com.meu_casamento.meu_casamento.domain.Produto;
import com.meu_casamento.meu_casamento.domain.TentativaPagamento;
import com.meu_casamento.meu_casamento.domain.Usuario;
import com.meu_casamento.meu_casamento.dto.PagamentoWebhook;
import com.meu_casamento.meu_casamento.dto.UsuarioRequest;
import com.meu_casamento.meu_casamento.dto.WebhookResponse;
import com.meu_casamento.meu_casamento.exception.RecursoNaoEncontradoException;
import com.meu_casamento.meu_casamento.repository.ProdutoRepository;
import com.meu_casamento.meu_casamento.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class WebhookPagamentoService {

    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final WebhookEventoService webhookEventoService;
    private final TentativaPagamentoService tentativaPagamentoService;
    
    public WebhookPagamentoService(ProdutoRepository produtoRepository, UsuarioRepository usuarioRepository,
                                  WebhookEventoService webhookEventoService, TentativaPagamentoService tentativaPagamentoService) { 
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.webhookEventoService = webhookEventoService;
        this.tentativaPagamentoService = tentativaPagamentoService;
    }

    @Transactional
    public WebhookResponse processar(PagamentoWebhook webhook) {
        try {
            log.info("Processando webhook da InfinitePay: invoice_slug={}, order_nsu={}, amount={}, paid_amount={}", 
                    webhook.getInvoiceSlug(), webhook.getOrderNsu(), webhook.getAmount(), webhook.getPaidAmount());

            // Verificar se o pagamento foi aprovado
            if (!isPagamentoAprovado(webhook)) {
                log.info("Pagamento não foi aprovado. Ignorando webhook.");
                webhookEventoService.registrar("infinitepay", webhook, null, false, true, null);
                return WebhookResponse.sucesso();
            }

            // Extrair o ID do produto do order_nsu
            UUID produtoId = extrairProdutoId(webhook.getOrderNsu());
            
            // Buscar o produto
            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + produtoId));

            // Verificar se o produto já está reservado
            if (produto.isStatusReservado()) {
                log.warn("Produto {} já está reservado. Ignorando webhook.", produtoId);
                webhookEventoService.registrar("infinitepay", webhook, produtoId, true, true, null);
                return WebhookResponse.sucesso();
            }

            // Processar tentativa de pagamento direto (se existir)
            Optional<TentativaPagamento> tentativaOpt = tentativaPagamentoService.marcarComoPago(produtoId);
            
            if (tentativaOpt.isPresent()) {
                // Era pagamento direto - criar usuário definitivo agora que foi pago
                TentativaPagamento tentativa = tentativaOpt.get();
                criarUsuarioDefinitivo(tentativa);
                log.info("Tentativa de pagamento direto confirmada e usuário criado para produto {}", produtoId);
            }

            // Marcar produto como reservado
            produto.setStatusReservado(true);
            produtoRepository.save(produto);

            log.info("Produto {} marcado como reservado com sucesso via webhook da InfinitePay", produtoId);
            webhookEventoService.registrar("infinitepay", webhook, produtoId, true, true, null);
            
            return WebhookResponse.sucesso();

        } catch (IllegalArgumentException e) {
            log.error("Erro ao processar order_nsu: {}", webhook.getOrderNsu(), e);
            webhookEventoService.registrar("infinitepay", webhook, null, true, true, "Formato inválido do order_nsu");
            return WebhookResponse.sucesso();
        } catch (RecursoNaoEncontradoException e) {
            log.error("Produto não encontrado: {}", e.getMessage());
            webhookEventoService.registrar("infinitepay", webhook, null, true, true, "Produto não encontrado");
            return WebhookResponse.sucesso();
        } catch (Exception e) {
            log.error("Erro inesperado ao processar webhook", e);
            webhookEventoService.registrar("infinitepay", webhook, null, false, false, "Erro interno do servidor");
            return WebhookResponse.erro("Erro interno do servidor");
        }
    }

    private boolean isPagamentoAprovado(PagamentoWebhook webhook) {
        // Verificar se o valor pago é maior ou igual ao valor esperado
        Integer valorEsperado = webhook.getAmount();
        Integer valorPago = webhook.getPaidAmount();
        
        if (valorEsperado == null || valorPago == null) {
            return false;
        }
        
        return valorPago >= valorEsperado;
    }

    private UUID extrairProdutoId(String orderNsu) {
        // Assumindo que o order_nsu é o UUID do produto
        // Se você usar uma estratégia diferente, ajuste aqui
        return UUID.fromString(orderNsu);
    }

    private void criarUsuarioDefinitivo(TentativaPagamento tentativa) {
        // Verificar se usuário já existe
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(tentativa.getEmail());
        
        if (usuarioExistente.isPresent()) {
            // Adicionar produto à lista do usuário existente
            Usuario usuario = usuarioExistente.get();
            if (!usuario.getProdutosReservados().contains(tentativa.getProdutoId())) {
                usuario.getProdutosReservados().add(tentativa.getProdutoId());
                usuarioRepository.save(usuario);
                log.info("Produto {} adicionado ao usuário existente {}", tentativa.getProdutoId(), usuario.getId());
            }
        } else {
            // Criar novo usuário
            Usuario novoUsuario = Usuario.builder()
                    .nome(tentativa.getNome())
                    .telefone(tentativa.getTelefone())
                    .email(tentativa.getEmail())
                    .mensagem(tentativa.getMensagem())
                    .meioReserva(Usuario.MeioReserva.pagamentoDireto)
                    .produtosReservados(List.of(tentativa.getProdutoId()))
                    .build();
            usuarioRepository.save(novoUsuario);
            log.info("Novo usuário criado para tentativa de pagamento direto: {}", novoUsuario.getId());
        }
    }
}
