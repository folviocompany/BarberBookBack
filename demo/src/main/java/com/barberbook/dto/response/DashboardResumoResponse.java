package com.barberbook.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record DashboardResumoResponse(
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate data,

    long totalAgendamentos,
    long totalPendentes,
    long totalConfirmados,
    long totalCancelados,
    long totalConcluidos,
    List<AgendamentoResponse> agendamentosDodia
) {}
