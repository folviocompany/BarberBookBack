package com.barberbook.repository;

import com.barberbook.entity.Bloqueio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BloqueioRepository extends JpaRepository<Bloqueio, UUID> {

    List<Bloqueio> findByBarbeiroIdAndData(UUID barbeiroId, LocalDate data);

    /**
     * JOIN FETCH no barbeiro evita N+1 lazy ao montar BloqueioResponse.
     * Navegação bar.barbearia.id no WHERE usa JOIN implícito do JPQL — sem SELECT extra.
     */
    @Query("""
            SELECT b FROM Bloqueio b
            JOIN FETCH b.barbeiro bar
            WHERE bar.barbearia.id = :barbeariaId
              AND (:barbeiroId IS NULL OR bar.id = :barbeiroId)
              AND (:data IS NULL OR b.data = :data)
            ORDER BY b.data ASC, b.inicio ASC
            """)
    List<Bloqueio> findFiltrado(
            @Param("barbeariaId") UUID barbeariaId,
            @Param("barbeiroId") UUID barbeiroId,
            @Param("data") LocalDate data
    );

    /**
     * Ownership check em query única — evita cadeia de 3 lazy loads (Bloqueio→Barbeiro→Barbearia).
     */
    @Query("""
            SELECT b FROM Bloqueio b
            WHERE b.id = :id
              AND b.barbeiro.barbearia.id = :barbeariaId
            """)
    Optional<Bloqueio> findByIdAndBarbeariaId(
            @Param("id") UUID id,
            @Param("barbeariaId") UUID barbeariaId
    );

    /**
     * Verifica sobreposição entre um slot [slotInicio, slotFim) e bloqueios existentes.
     * Há conflito quando: bloqueio.inicio < slotFim AND bloqueio.fim > slotInicio
     */
    @Query("""
            SELECT COUNT(b) > 0 FROM Bloqueio b
            WHERE b.barbeiro.id = :barbeiroId
              AND b.data        = :data
              AND b.inicio      < :slotFim
              AND b.fim         > :slotInicio
            """)
    boolean existsBloqueioConflito(
            @Param("barbeiroId") UUID barbeiroId,
            @Param("data") LocalDate data,
            @Param("slotInicio") LocalTime slotInicio,
            @Param("slotFim") LocalTime slotFim
    );
}
