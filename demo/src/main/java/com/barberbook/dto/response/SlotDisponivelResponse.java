package com.barberbook.dto.response;

import java.time.LocalTime;
import java.util.List;

public record SlotDisponivelResponse(
    LocalTime horario,
    List<BarbeiroResumoResponse> barbeirosDisponiveis
) {}
