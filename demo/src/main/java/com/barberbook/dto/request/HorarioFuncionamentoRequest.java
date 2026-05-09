package com.barberbook.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record HorarioFuncionamentoRequest(
    @Min(0) @Max(6) int diaSemana,

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    LocalTime abertura,

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    LocalTime fechamento
) {}
