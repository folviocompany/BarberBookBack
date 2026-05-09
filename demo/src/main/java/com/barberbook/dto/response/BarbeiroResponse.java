package com.barberbook.dto.response;

import java.util.UUID;

public record BarbeiroResponse(
    UUID id,
    String nome,
    String fotoUrl
) {}
