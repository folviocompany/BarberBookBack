package com.barberbook.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ServicoAdminResponse(
    UUID id,
    String nome,
    int duracaoMinutos,
    BigDecimal preco,
    boolean ativo
) {}
