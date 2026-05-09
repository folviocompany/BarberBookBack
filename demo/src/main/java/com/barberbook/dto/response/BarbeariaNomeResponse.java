package com.barberbook.dto.response;

import java.util.UUID;

public record BarbeariaNomeResponse(
    UUID id,
    String nome,
    String slug
) {}
