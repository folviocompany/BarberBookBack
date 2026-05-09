package com.barberbook.controller.admin;

import com.barberbook.dto.request.AtualizarBarbeariaRequest;
import com.barberbook.dto.request.HorarioFuncionamentoRequest;
import com.barberbook.dto.request.LogoRequest;
import com.barberbook.dto.response.BarbeariaAdminResponse;
import com.barberbook.security.SecurityUtils;
import com.barberbook.service.BarbeariaAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/barbearia")
@RequiredArgsConstructor
public class BarbeariaAdminController {

    private final BarbeariaAdminService service;

    @GetMapping
    public ResponseEntity<BarbeariaAdminResponse> buscar() {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.ok(service.buscar(barbeariaId));
    }

    @PutMapping
    public ResponseEntity<BarbeariaAdminResponse> atualizar(@Valid @RequestBody AtualizarBarbeariaRequest request) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.ok(service.atualizar(barbeariaId, request));
    }

    @PutMapping("/horarios")
    public ResponseEntity<BarbeariaAdminResponse> atualizarHorarios(
            @Valid @RequestBody List<HorarioFuncionamentoRequest> horarios) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.ok(service.atualizarHorarios(barbeariaId, horarios));
    }

    @PostMapping("/logo")
    public ResponseEntity<BarbeariaAdminResponse> atualizarLogo(@Valid @RequestBody LogoRequest request) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.ok(service.atualizarLogo(barbeariaId, request));
    }
}
