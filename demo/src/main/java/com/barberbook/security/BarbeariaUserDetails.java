package com.barberbook.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class BarbeariaUserDetails implements UserDetails {

    private final String email;
    private final UUID barbeariaId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_BARBEARIA"));
    }

    @Override public String getPassword()  { return null; }
    @Override public String getUsername()  { return email; }
}
