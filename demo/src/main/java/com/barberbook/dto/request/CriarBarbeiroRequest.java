package com.barberbook.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CriarBarbeiroRequest(
    @NotBlank String nome,
    String fotoUrl
) {}
