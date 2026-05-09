package com.barberbook.controller.admin;

import com.barberbook.dto.request.AtualizarServicoRequest;
import com.barberbook.dto.request.CriarServicoRequest;
import com.barberbook.dto.response.ServicoAdminResponse;
import com.barberbook.security.SecurityUtils;
import com.barberbook.service.ServicoAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/servicos")
@RequiredArgsConstructor
public class ServicoAdminController {

    private final ServicoAdminService service;

    @GetMapping
    public ResponseEntity<List<ServicoAdminResponse>> listar() {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.ok(service.listar(barbeariaId));
    }

    @PostMapping
    public ResponseEntity<ServicoAdminResponse> criar(@Valid @RequestBody CriarServicoRequest request) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(barbeariaId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoAdminResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarServicoRequest request) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.ok(service.atualizar(barbeariaId, id, request));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<ServicoAdminResponse> alternarAtivo(@PathVariable UUID id) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.ok(service.alternarAtivo(barbeariaId, id));
    }
}
