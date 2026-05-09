package com.barberbook.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CriarAgendamentoRequest(

    @NotBlank
    String slugBarbearia,

    @NotNull
    UUID barbeiroId,

    @NotNull
    UUID servicoId,

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate data,

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    LocalTime horario,

    @NotBlank
    String clienteNome,

    @NotBlank
    String clienteTelefone
) {}
