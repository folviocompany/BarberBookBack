package com.barberbook.repository;

import com.barberbook.entity.Agendamento;
import com.barberbook.entity.AgendamentoStatus;
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
public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

    List<Agendamento> findByBarbeariaIdAndData(UUID barbeariaId, LocalDate data);

    List<Agendamento> findByBarbeiroIdAndData(UUID barbeiroId, LocalDate data);

    List<Agendamento> findByBarbeariaIdAndDataBetween(UUID barbeariaId, LocalDate inicio, LocalDate fim);

    List<Agendamento> findByBarbeariaIdAndStatus(UUID barbeariaId, AgendamentoStatus status);

    long countByBarbeariaIdAndData(UUID barbeariaId, LocalDate data);

    long countByBarbeariaIdAndDataAndStatus(UUID barbeariaId, LocalDate data, AgendamentoStatus status);

    /** Ownership check sem lazy load — barbeariaId já está na própria tabela agendamentos. */
    Optional<Agendamento> findByIdAndBarbeariaId(UUID id, UUID barbeariaId);

    /**
     * JOIN FETCH em barbeiro, servico e barbearia evita N×3 lazy queries no AgendamentoMapper.
     */
    @Query("""
            SELECT a FROM Agendamento a
            JOIN FETCH a.barbeiro
            JOIN FETCH a.servico
            JOIN FETCH a.barbearia
            WHERE a.barbearia.id = :barbeariaId
              AND (:data IS NULL OR a.data = :data)
              AND (:barbeiroId IS NULL OR a.barbeiro.id = :barbeiroId)
              AND (:status IS NULL OR a.status = :status)
            ORDER BY a.data ASC, a.horario ASC
            """)
    List<Agendamento> findFiltrado(
            @Param("barbeariaId") UUID barbeariaId,
            @Param("data") LocalDate data,
            @Param("barbeiroId") UUID barbeiroId,
            @Param("status") AgendamentoStatus status
    );

    /**
     * Verifica sobreposição entre um slot [slotInicio, slotFim) e agendamentos existentes.
     * Há conflito quando: agendamento.horario < slotFim
     *                 AND agendamento.horario + duracao > slotInicio
     */
    @Query(value = """
            SELECT EXISTS (
                SELECT 1
                FROM agendamentos a
                JOIN servicos s ON a.servico_id = s.id
                WHERE a.barbeiro_id = :barbeiroId
                  AND a.data        = :data
                  AND a.status      IN :statusAtivos
                  AND a.horario     < :slotFim
                  AND (a.horario + make_interval(mins => s.duracao_minutos)) > :slotInicio
            )
            """, nativeQuery = true)
    boolean existsConflito(
            @Param("barbeiroId") UUID barbeiroId,
            @Param("data") LocalDate data,
            @Param("slotInicio") LocalTime slotInicio,
            @Param("slotFim") LocalTime slotFim,
            @Param("statusAtivos") List<String> statusAtivos
    );
}
