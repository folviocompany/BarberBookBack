package com.barberbook.dto.request;

import com.barberbook.entity.AgendamentoStatus;
import jakarta.validation.constraints.NotNull;

public record AlterarStatusRequest(
    @NotNull AgendamentoStatus status
) {}
