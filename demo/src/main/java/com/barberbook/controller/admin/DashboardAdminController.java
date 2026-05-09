package com.barberbook.controller.admin;

import com.barberbook.dto.response.DashboardResumoResponse;
import com.barberbook.security.SecurityUtils;
import com.barberbook.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class DashboardAdminController {

    private final DashboardService service;

    @GetMapping("/resumo")
    public ResponseEntity<DashboardResumoResponse> resumo(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        UUID barbeariaId = SecurityUtils.getBarbeariaIdAutenticada();
        LocalDate dataConsulta = data != null ? data : LocalDate.now();
        return ResponseEntity.ok(service.resumo(barbeariaId, dataConsulta));
    }
}
