package com.barberbook.service;

import com.barberbook.dto.request.AlterarStatusRequest;
import com.barberbook.dto.response.AgendamentoResponse;
import com.barberbook.entity.Agendamento;
import com.barberbook.entity.AgendamentoStatus;
import com.barberbook.exception.ConflictException;
import com.barberbook.exception.EntityNotFoundException;
import com.barberbook.repository.AgendamentoRepository;
import com.barberbook.util.AgendamentoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgendamentoAdminService {

    private final AgendamentoRepository agendamentoRepository;

    public List<AgendamentoResponse> listar(UUID barbeariaId, LocalDate data, UUID barbeiroId, AgendamentoStatus status) {
        return agendamentoRepository.findFiltrado(barbeariaId, data, barbeiroId, status)
                .stream()
                .map(AgendamentoMapper::toResponse)
                .toList();
    }

    public AgendamentoResponse buscarPorId(UUID barbeariaId, UUID agendamentoId) {
        return AgendamentoMapper.toResponse(buscarComOwnership(barbeariaId, agendamentoId));
    }

    @Transactional
    public AgendamentoResponse alterarStatus(UUID barbeariaId, UUID agendamentoId, AlterarStatusRequest request) {
        Agendamento a = buscarComOwnership(barbeariaId, agendamentoId);

        if (request.status() == AgendamentoStatus.PENDENTE) {
            throw new ConflictException("Não é permitido voltar para o status PENDENTE");
        }

        a.setStatus(request.status());
        return AgendamentoMapper.toResponse(agendamentoRepository.save(a));
    }

    /** Busca agendamento da barbearia sem disparar lazy load — usa FK direta na query. */
    private Agendamento buscarComOwnership(UUID barbeariaId, UUID agendamentoId) {
        return agendamentoRepository.findByIdAndBarbeariaId(agendamentoId, barbeariaId)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
    }
}
