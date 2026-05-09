package com.barberbook.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ServicoResponse(
    UUID id,
    String nome,
    int duracaoMinutos,
    BigDecimal preco
) {}
