package com.meu_casamento.meu_casamento.controller;

import com.meu_casamento.meu_casamento.dto.UsuarioRequest;
import com.meu_casamento.meu_casamento.dto.UsuarioResponse;
import com.meu_casamento.meu_casamento.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    @PostMapping("/reservar")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse salvarReserva(@RequestBody @Valid UsuarioRequest request) {
        return usuarioService.salvarReserva(request);
    }
    
    @GetMapping
    public List<UsuarioResponse> listarTodos() {
        return usuarioService.listarTodos();
    }
    
    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable UUID id) {
        return usuarioService.buscarPorId(id);
    }
    
    @GetMapping("/email/{email}")
    public UsuarioResponse buscarPorEmail(@PathVariable String email) {
        return usuarioService.buscarPorEmail(email);
    }
}
