package com.barberbook.dto.response;

import java.util.UUID;

public record BarbeiroResumoResponse(
    UUID id,
    String nome,
    String fotoUrl
) {}
