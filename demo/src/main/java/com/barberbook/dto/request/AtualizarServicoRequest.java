package com.barberbook.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AtualizarServicoRequest(
    @NotBlank String nome,
    @Min(5) int duracaoMinutos,
    @NotNull @DecimalMin("0.01") BigDecimal preco
) {}
