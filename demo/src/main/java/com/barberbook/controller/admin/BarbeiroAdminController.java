package com.barberbook.controller.admin;

import com.barberbook.dto.request.AtualizarBarbeiroRequest;
import com.barberbook.dto.request.CriarBarbeiroRequest;
import com.barberbook.dto.response.BarbeiroAdminResponse;
import com.barberbook.security.SecurityUtils;
import com.barberbook.service.BarbeiroAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/barbeiros")
@RequiredArgsConstructor
public class BarbeiroAdminController {

    private final BarbeiroAdminService service;

    @GetMapping
    public ResponseEntity<List<BarbeiroAdminResponse>> listar() {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.ok(service.listar(barbeariaId));
    }

    @PostMapping
    public ResponseEntity<BarbeiroAdminResponse> criar(@Valid @RequestBody CriarBarbeiroRequest request) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(barbeariaId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarbeiroAdminResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarBarbeiroRequest request) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.ok(service.atualizar(barbeariaId, id, request));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<BarbeiroAdminResponse> alternarAtivo(@PathVariable UUID id) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.ok(service.alternarAtivo(barbeariaId, id));
    }
}
