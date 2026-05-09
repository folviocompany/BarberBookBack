package com.barberbook.dto.response;

import java.util.List;
import java.util.UUID;

public record BarbeariaAdminResponse(
    UUID id,
    String nome,
    String slug,
    String telefone,
    String endereco,
    String logoUrl,
    String email,
    String status,
    List<HorarioFuncionamentoResponse> horariosFuncionamento
) {}
