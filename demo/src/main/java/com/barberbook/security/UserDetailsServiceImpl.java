package com.barberbook.security;

import com.barberbook.entity.Barbearia;
import com.barberbook.repository.BarbeariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;

/**
 * Mantido por compatibilidade com o AuthenticationManager auto-configurado pelo Spring Security.
 * NÃO é usado no fluxo JWT — a autenticação ocorre diretamente em AuthService + JwtAuthFilter.
 * Removendo o @Service para evitar que o DaoAuthenticationProvider seja auto-configurado.
 */
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final BarbeariaRepository barbeariaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Barbearia barbearia = barbeariaRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Barbearia não encontrada: " + email));

        return new User(
            barbearia.getEmail(),
            barbearia.getSenhaHash(),
            List.of(new SimpleGrantedAuthority("ROLE_BARBEARIA"))
        );
    }
}
