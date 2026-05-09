package com.barberbook.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LogoRequest(
    @NotBlank
    @Pattern(regexp = "^https?://.*", message = "URL deve começar com http:// ou https://")
    String logoUrl
) {}
