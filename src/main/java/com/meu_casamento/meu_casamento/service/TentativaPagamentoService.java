package com.meu_casamento.meu_casamento.service;

import com.meu_casamento.meu_casamento.domain.TentativaPagamento;
import com.meu_casamento.meu_casamento.dto.UsuarioRequest;
import com.meu_casamento.meu_casamento.repository.TentativaPagamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TentativaPagamentoService {

    private final TentativaPagamentoRepository repository;

    @Transactional
    public TentativaPagamento criarTentativa(UsuarioRequest request) {
        // Verificar se já existe tentativa PENDENTE para este email+produto
        Optional<TentativaPagamento> tentativaExistente = repository
                .findByEmailAndProdutoIdAndStatus(request.getEmail(), request.getProdutoId(), 
                        TentativaPagamento.StatusPagamento.PENDENTE);

        if (tentativaExistente.isPresent()) {
            log.info("Reutilizando tentativa existente para email {} e produto {}", 
                    request.getEmail(), request.getProdutoId());
            return tentativaExistente.get();
        }

        // Criar nova tentativa
        TentativaPagamento tentativa = TentativaPagamento.builder()
                .nome(request.getNome())
                .telefone(request.getTelefone())
                .email(request.getEmail())
                .mensagem(request.getMensagem())
                .produtoId(request.getProdutoId())
                .status(TentativaPagamento.StatusPagamento.PENDENTE)
                .build();

        TentativaPagamento salva = repository.save(tentativa);
        log.info("Nova tentativa de pagamento criada: {}", salva.getId());
        return salva;
    }

    @Transactional
    public void definirOrderNsu(UUID tentativaId, String orderNsu) {
        repository.findById(tentativaId).ifPresent(tentativa -> {
            tentativa.setOrderNsu(orderNsu);
            repository.save(tentativa);
            log.info("Order NSU {} definido para tentativa {}", orderNsu, tentativaId);
        });
    }

    @Transactional
    public Optional<TentativaPagamento> marcarComoPago(UUID produtoId) {
        // Buscar tentativa PENDENTE para este produto
        return repository.findByProdutoIdAndStatus(produtoId, TentativaPagamento.StatusPagamento.PENDENTE)
                .stream()
                .findFirst()
                .map(tentativa -> {
                    tentativa.setStatus(TentativaPagamento.StatusPagamento.PAGO);
                    tentativa.setPagoEm(LocalDateTime.now());
                    TentativaPagamento atualizada = repository.save(tentativa);
                    log.info("Tentativa {} marcada como PAGO para produto {}", tentativa.getId(), produtoId);
                    return atualizada;
                });
    }

    @Transactional
    public Optional<TentativaPagamento> buscarPorOrderNsu(String orderNsu) {
        return repository.findByOrderNsu(orderNsu);
    }
}
