package com.barberbook.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    int status,
    String erro,
    OffsetDateTime timestamp,
    List<FieldError> campos
) {
    public record FieldError(String campo, String mensagem) {}

    public static ErrorResponse of(int status, String erro) {
        return new ErrorResponse(status, erro, OffsetDateTime.now(), null);
    }

    public static ErrorResponse of(int status, String erro, List<FieldError> campos) {
        return new ErrorResponse(status, erro, OffsetDateTime.now(), campos);
    }
}
