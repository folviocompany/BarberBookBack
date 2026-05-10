package com.barberbook.service;

import com.barberbook.dto.request.LoginRequest;
import com.barberbook.dto.response.BarbeariaNomeResponse;
import com.barberbook.dto.response.LoginResponse;
import com.barberbook.entity.Barbearia;
import com.barberbook.exception.UnauthorizedException;
import com.barberbook.repository.BarbeariaRepository;
import com.barberbook.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final BarbeariaRepository barbeariaRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        log.info("=== DEBUG LOGIN ===");
        log.info("Email recebido: '{}'", request.email());

        Barbearia barbearia = barbeariaRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.info("Barbearia encontrada: false");
                    log.info("===================");
                    return new UnauthorizedException("Credenciais inválidas");
                });

        log.info("Barbearia encontrada: true");
        log.info("Hash no banco: '{}'", barbearia.getSenhaHash());
        log.info("Tamanho do hash: {}", barbearia.getSenhaHash().length());
        log.info("Senha recebida: '{}'", request.senha());
        boolean matches = passwordEncoder.matches(request.senha(), barbearia.getSenhaHash());
        log.info("Senha bate: {}", matches);
        log.info("===================");

        if (!matches) {
            throw new UnauthorizedException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(barbearia.getEmail(), barbearia.getId());
        return new LoginResponse(token, toBarbeariaNome(barbearia));
    }

    public LoginResponse refresh(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token ausente");
        }
        String oldToken = authHeader.substring(7);

        if (!jwtService.isTokenValid(oldToken)) {
            throw new UnauthorizedException("Token inválido ou expirado");
        }

        String email = jwtService.extractEmail(oldToken);
        Barbearia barbearia = barbeariaRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Barbearia não encontrada"));

        String newToken = jwtService.generateToken(barbearia.getEmail(), barbearia.getId());
        return new LoginResponse(newToken, toBarbeariaNome(barbearia));
    }

    private BarbeariaNomeResponse toBarbeariaNome(Barbearia b) {
        return new BarbeariaNomeResponse(b.getId(), b.getNome(), b.getSlug());
    }
}
