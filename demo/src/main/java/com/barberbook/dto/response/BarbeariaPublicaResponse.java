package com.barberbook.dto.response;

import java.util.List;
import java.util.UUID;

public record BarbeariaPublicaResponse(
    UUID id,
    String nome,
    String slug,
    String telefone,
    String endereco,
    String logoUrl,
    List<HorarioFuncionamentoResponse> horariosFuncionamento
) {}
