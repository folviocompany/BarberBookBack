package com.barberbook.security;

import com.barberbook.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {

    private SecurityUtils() {}

    public static UUID getBarbeariaIdAutenticada() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof BarbeariaUserDetails principal)) {
            throw new UnauthorizedException("Não autenticado");
        }
        return principal.getBarbeariaId();
    }
}
