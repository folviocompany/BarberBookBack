package com.barberbook.repository;

import com.barberbook.entity.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BarbeiroRepository extends JpaRepository<Barbeiro, UUID> {

    List<Barbeiro> findByBarbeariaId(UUID barbeariaId);

    List<Barbeiro> findByBarbeariaIdAndAtivoTrue(UUID barbeariaId);

    Optional<Barbeiro> findByIdAndBarbeariaId(UUID id, UUID barbeariaId);

    /**
     * Ownership + ativo em uma única query — evita lazy load de Barbearia para validação
     * (usado em SlotService.resolverBarbeiros).
     */
    Optional<Barbeiro> findByIdAndBarbeariaIdAndAtivoTrue(UUID id, UUID barbeariaId);
}
