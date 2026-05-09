package com.barberbook.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AtualizarBarbeariaRequest(
    @NotBlank String nome,
    @NotBlank String telefone,
    @NotBlank String endereco
) {}
