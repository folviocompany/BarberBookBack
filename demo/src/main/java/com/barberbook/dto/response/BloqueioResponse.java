package com.barberbook.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record BloqueioResponse(
    UUID id,
    UUID barbeiroId,
    String barbeiroNome,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate data,

    @JsonFormat(pattern = "HH:mm")
    LocalTime inicio,

    @JsonFormat(pattern = "HH:mm")
    LocalTime fim,

    String motivo
) {}
