package com.barberbook.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {

    private String secret;
    private int expirationHours = 8;

    @PostConstruct
    public void validate() {
        if (secret == null || secret.isBlank() || secret.length() < 32) {
            throw new IllegalStateException(
                    "Configuração inválida: app.jwt.secret deve ter no mínimo 32 caracteres. " +
                    "Defina a variável de ambiente JWT_SECRET em produção.");
        }
    }
}
