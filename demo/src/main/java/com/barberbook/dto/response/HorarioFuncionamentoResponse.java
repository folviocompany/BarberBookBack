package com.barberbook.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record HorarioFuncionamentoResponse(
    int diaSemana,

    @JsonFormat(pattern = "HH:mm")
    LocalTime abertura,

    @JsonFormat(pattern = "HH:mm")
    LocalTime fechamento
) {}
