package com.barberbook.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CriarBloqueioRequest(
    @NotNull UUID barbeiroId,

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate data,

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    LocalTime inicio,

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    LocalTime fim,

    String motivo
) {}
