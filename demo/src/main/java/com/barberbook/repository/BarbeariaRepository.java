package com.barberbook.repository;

import com.barberbook.entity.Barbearia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BarbeariaRepository extends JpaRepository<Barbearia, UUID> {

    Optional<Barbearia> findByEmail(String email);

    Optional<Barbearia> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByEmail(String email);
}
