package com.barberbook.dto.response;

public record LoginResponse(
    String token,
    BarbeariaNomeResponse barbearia
) {}
