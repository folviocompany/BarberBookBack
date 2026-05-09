package com.barberbook.dto.response;

import java.util.UUID;

public record BarbeiroAdminResponse(
    UUID id,
    String nome,
    String fotoUrl,
    boolean ativo
) {}
