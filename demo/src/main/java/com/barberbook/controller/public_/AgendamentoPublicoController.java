package com.barberbook.controller.public_;

import com.barberbook.dto.request.CriarAgendamentoRequest;
import com.barberbook.dto.response.AgendamentoResponse;
import com.barberbook.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/public/agendamentos")
@RequiredArgsConstructor
public class AgendamentoPublicoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(@Valid @RequestBody CriarAgendamentoRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(agendamentoService.criarAgendamento(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AgendamentoResponse> cancelar(@PathVariable UUID id) {
        return ResponseEntity.ok(agendamentoService.cancelar(id));
    }
}
