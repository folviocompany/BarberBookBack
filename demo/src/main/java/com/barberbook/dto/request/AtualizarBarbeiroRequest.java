package com.barberbook.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AtualizarBarbeiroRequest(
    @NotBlank String nome,
    String fotoUrl
) {}
