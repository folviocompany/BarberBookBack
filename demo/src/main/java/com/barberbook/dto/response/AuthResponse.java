package com.barberbook.dto.response;

import java.util.UUID;

public record AuthResponse(
    String token,
    String email,
    UUID barbeariaId,
    String nomeBarbearia
) {}
