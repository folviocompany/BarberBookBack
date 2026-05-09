package com.barberbook.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.barberbook.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;

    public String generateToken(String email, UUID barbeariaId) {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());

        return JWT.create()
            .withSubject(email)
            .withClaim("barbearia_id", barbeariaId.toString())
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plus(jwtConfig.getExpirationHours(), ChronoUnit.HOURS))
            .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
        return JWT.require(algorithm)
            .build()
            .verify(token);
    }

    public String extractEmail(String token) {
        return validateToken(token).getSubject();
    }

    public UUID extractBarbeariaId(String token) {
        String id = validateToken(token).getClaim("barbearia_id").asString();
        return UUID.fromString(id);
    }

    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
