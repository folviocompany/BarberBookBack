package com.barberbook.repository;

import com.barberbook.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, UUID> {

    List<Servico> findByBarbeariaId(UUID barbeariaId);

    List<Servico> findByBarbeariaIdAndAtivoTrue(UUID barbeariaId);

    Optional<Servico> findByIdAndBarbeariaId(UUID id, UUID barbeariaId);
}
