package com.barberbook.config;

import com.barberbook.entity.Barbearia;
import com.barberbook.repository.BarbeariaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDataInitializer implements CommandLineRunner {

    private final BarbeariaRepository barbeariaRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String TEST_EMAIL = "teste@barberbook.com";
    private static final String TEST_SENHA = "123456";

    @Override
    public void run(String... args) {
        barbeariaRepository.findByEmail(TEST_EMAIL).ifPresentOrElse(
            barbearia -> {
                // matches() verifica antes de re-encodar — BCrypt é lento (~200ms), não recalcular desnecessariamente
                if (!passwordEncoder.matches(TEST_SENHA, barbearia.getSenhaHash())) {
                    barbearia.setSenhaHash(passwordEncoder.encode(TEST_SENHA));
                    barbeariaRepository.save(barbearia);
                    log.info("[DEV] Hash da barbearia '{}' atualizado com BCrypt correto", TEST_EMAIL);
                }
            },
            () -> {
                Barbearia barbearia = Barbearia.builder()
                        .nome("Barbearia Teste")
                        .slug("teste")
                        .email(TEST_EMAIL)
                        .senhaHash(passwordEncoder.encode(TEST_SENHA))
                        .telefone("(11) 99999-9999")
                        .status("ATIVO")
                        .build();
                barbeariaRepository.save(barbearia);
                log.info("[DEV] Barbearia de teste criada: email={}, senha={}", TEST_EMAIL, TEST_SENHA);
            }
        );
    }
}
