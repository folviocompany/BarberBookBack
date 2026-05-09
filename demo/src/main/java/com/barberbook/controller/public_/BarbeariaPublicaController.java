package com.barberbook.controller.public_;

import com.barberbook.dto.response.*;
import com.barberbook.service.BarbeariaService;
import com.barberbook.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/barbearias")
@RequiredArgsConstructor
public class BarbeariaPublicaController {

    private final BarbeariaService barbeariaService;
    private final SlotService slotService;

    @GetMapping("/{slug}")
    public ResponseEntity<BarbeariaPublicaResponse> buscarPorSlug(@PathVariable String slug) {
        return ResponseEntity.ok(barbeariaService.buscarPorSlug(slug));
    }

    @GetMapping("/{slug}/servicos")
    public ResponseEntity<List<ServicoResponse>> listarServicos(@PathVariable String slug) {
        return ResponseEntity.ok(barbeariaService.listarServicosAtivos(slug));
    }

    @GetMapping("/{slug}/barbeiros")
    public ResponseEntity<List<BarbeiroResponse>> listarBarbeiros(@PathVariable String slug) {
        return ResponseEntity.ok(barbeariaService.listarBarbeirosAtivos(slug));
    }

    @GetMapping("/{slug}/horarios-disponiveis")
    public ResponseEntity<List<SlotDisponivelResponse>> horariosDisponiveis(
            @PathVariable String slug,
            @RequestParam UUID servicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) UUID barbeiroId) {

        return ResponseEntity.ok(slotService.calcularSlotsDisponiveis(slug, data, servicoId, barbeiroId));
    }
}
