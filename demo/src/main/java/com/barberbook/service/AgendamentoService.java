package com.barberbook.service;

import com.barberbook.dto.request.CriarAgendamentoRequest;
import com.barberbook.dto.response.AgendamentoResponse;
import com.barberbook.entity.*;
import com.barberbook.exception.ConflictException;
import com.barberbook.exception.EntityNotFoundException;
import com.barberbook.repository.*;
import com.barberbook.util.AgendamentoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final BarbeariaRepository barbeariaRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final ServicoRepository servicoRepository;
    private final SlotService slotService;

    @Transactional
    public AgendamentoResponse criarAgendamento(CriarAgendamentoRequest request) {
        // Revalida disponibilidade antes de salvar
        boolean slotDisponivel = slotService
                .calcularSlotsDisponiveis(
                        request.slugBarbearia(),
                        request.data(),
                        request.servicoId(),
                        request.barbeiroId())
                .stream()
                .filter(s -> s.horario().equals(request.horario()))
                .flatMap(s -> s.barbeirosDisponiveis().stream())
                .anyMatch(b -> b.id().equals(request.barbeiroId()));

        if (!slotDisponivel) {
            throw new ConflictException("Horário não disponível para o barbeiro selecionado.");
        }

        Barbearia barbearia = barbeariaRepository.findBySlug(request.slugBarbearia())
                .orElseThrow(() -> new EntityNotFoundException("Barbearia", request.slugBarbearia()));

        Barbeiro barbeiro = barbeiroRepository.findById(request.barbeiroId())
                .orElseThrow(() -> new EntityNotFoundException("Barbeiro", request.barbeiroId()));

        Servico servico = servicoRepository.findById(request.servicoId())
                .orElseThrow(() -> new EntityNotFoundException("Serviço", request.servicoId()));

        Agendamento agendamento = Agendamento.builder()
                .barbearia(barbearia)
                .barbeiro(barbeiro)
                .servico(servico)
                .clienteNome(request.clienteNome())
                .clienteTelefone(request.clienteTelefone())
                .data(request.data())
                .horario(request.horario())
                .status(AgendamentoStatus.PENDENTE)
                .build();

        return AgendamentoMapper.toResponse(agendamentoRepository.save(agendamento));
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(UUID id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento", id));

        return AgendamentoMapper.toResponse(agendamento);
    }

    @Transactional
    public AgendamentoResponse cancelar(UUID id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento", id));

        if (agendamento.getStatus() == AgendamentoStatus.CANCELADO
                || agendamento.getStatus() == AgendamentoStatus.CONCLUIDO) {
            throw new ConflictException(
                    "Agendamento não pode ser cancelado — status atual: " + agendamento.getStatus());
        }

        agendamento.setStatus(AgendamentoStatus.CANCELADO);
        return AgendamentoMapper.toResponse(agendamentoRepository.save(agendamento));
    }
}
