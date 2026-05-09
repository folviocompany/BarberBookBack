package com.barberbook.dto.response;

import com.barberbook.entity.AgendamentoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AgendamentoResponse(
    UUID id,
    String clienteNome,
    String clienteTelefone,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate data,

    @JsonFormat(pattern = "HH:mm")
    LocalTime horario,

    AgendamentoStatus status,
    ServicoResponse servico,
    BarbeiroResponse barbeiro,
    BarbeariaNomeResponse barbearia,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    OffsetDateTime criadoEm
) {}
