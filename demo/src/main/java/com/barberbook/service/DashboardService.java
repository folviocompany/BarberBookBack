package com.barberbook.service;

import com.barberbook.dto.response.AgendamentoResponse;
import com.barberbook.dto.response.DashboardResumoResponse;
import com.barberbook.entity.AgendamentoStatus;
import com.barberbook.repository.AgendamentoRepository;
import com.barberbook.util.AgendamentoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AgendamentoRepository agendamentoRepository;

    public DashboardResumoResponse resumo(UUID barbeariaId, LocalDate data) {
        long total       = agendamentoRepository.countByBarbeariaIdAndData(barbeariaId, data);
        long pendentes   = agendamentoRepository.countByBarbeariaIdAndDataAndStatus(barbeariaId, data, AgendamentoStatus.PENDENTE);
        long confirmados = agendamentoRepository.countByBarbeariaIdAndDataAndStatus(barbeariaId, data, AgendamentoStatus.CONFIRMADO);
        long cancelados  = agendamentoRepository.countByBarbeariaIdAndDataAndStatus(barbeariaId, data, AgendamentoStatus.CANCELADO);
        long concluidos  = agendamentoRepository.countByBarbeariaIdAndDataAndStatus(barbeariaId, data, AgendamentoStatus.CONCLUIDO);

        List<AgendamentoResponse> agendamentos = agendamentoRepository.findFiltrado(barbeariaId, data, null, null)
                .stream()
                .map(AgendamentoMapper::toResponse)
                .toList();

        return new DashboardResumoResponse(data, total, pendentes, confirmados, cancelados, concluidos, agendamentos);
    }
}
