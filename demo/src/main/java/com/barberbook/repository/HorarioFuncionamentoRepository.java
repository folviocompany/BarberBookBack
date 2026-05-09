package com.barberbook.repository;

import com.barberbook.entity.HorarioFuncionamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HorarioFuncionamentoRepository extends JpaRepository<HorarioFuncionamento, UUID> {

    List<HorarioFuncionamento> findByBarbeariaId(UUID barbeariaId);

    Optional<HorarioFuncionamento> findByBarbeariaIdAndDiaSemana(UUID barbeariaId, int diaSemana);

    @Modifying
    @Query("DELETE FROM HorarioFuncionamento h WHERE h.barbearia.id = :barbeariaId")
    void deleteByBarbeariaId(@Param("barbeariaId") UUID barbeariaId);
}
