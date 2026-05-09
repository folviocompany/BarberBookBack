package com.barberbook.controller.admin;

import com.barberbook.dto.request.CriarBloqueioRequest;
import com.barberbook.dto.response.BloqueioResponse;
import com.barberbook.security.SecurityUtils;
import com.barberbook.service.BloqueioAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/bloqueios")
@RequiredArgsConstructor
public class BloqueioAdminController {

    private final BloqueioAdminService service;

    @GetMapping
    public ResponseEntity<List<BloqueioResponse>> listar(
            @RequestParam(required = false) UUID barbeiroId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.ok(service.listar(barbeariaId, barbeiroId, data));
    }

    @PostMapping
    public ResponseEntity<BloqueioResponse> criar(@Valid @RequestBody CriarBloqueioRequest request) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(barbeariaId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        service.deletar(barbeariaId, id);
        return ResponseEntity.noContent().build();
    }
}
