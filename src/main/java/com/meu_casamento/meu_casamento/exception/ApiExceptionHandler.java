package com.meu_casamento.meu_casamento.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(RecursoNaoEncontradoException.class)
  public ResponseEntity<?> handleNotFound(RecursoNaoEncontradoException e){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleBeanValidation(MethodArgumentNotValidException e){
    Map<String, Object> body = new HashMap<>();
    body.put("erro", "Dados inválidos");
    body.put("detalhes", e.getBindingResult().getFieldErrors().stream()
          .map(fe -> Map.of("campo", fe.getField(), "mensagem", fe.getDefaultMessage()))
            .toList());
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleIllegalArg(IllegalArgumentException e){
    return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
  }
}
