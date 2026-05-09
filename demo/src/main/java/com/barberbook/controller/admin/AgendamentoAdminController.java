package com.barberbook.controller.admin;

import com.barberbook.dto.request.AlterarStatusRequest;
import com.barberbook.dto.response.AgendamentoResponse;
import com.barberbook.entity.AgendamentoStatus;
import com.barberbook.security.SecurityUtils;
import com.barberbook.service.AgendamentoAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/agendamentos")
@RequiredArgsConstructor
public class AgendamentoAdminController {

    private final AgendamentoAdminService service;

    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) UUID barbeiroId,
            @RequestParam(required = false) AgendamentoStatus status) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.ok(service.listar(barbeariaId, data, barbeiroId, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable UUID id) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.ok(service.buscarPorId(barbeariaId, id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AgendamentoResponse> alterarStatus(
            @PathVariable UUID id,
            @Valid @RequestBody AlterarStatusRequest request) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.ok(service.alterarStatus(barbeariaId, id, request));
    }
}
