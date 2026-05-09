package com.barberbook.service;

import com.barberbook.dto.response.BarbeiroResumoResponse;
import com.barberbook.dto.response.SlotDisponivelResponse;
import com.barberbook.entity.Barbeiro;
import com.barberbook.entity.Barbearia;
import com.barberbook.entity.HorarioFuncionamento;
import com.barberbook.entity.Servico;
import com.barberbook.exception.EntityNotFoundException;
import com.barberbook.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SlotService {

    private static final List<String> STATUS_OCUPADOS = List.of("PENDENTE", "CONFIRMADO");

    private final BarbeariaRepository barbeariaRepository;
    private final ServicoRepository servicoRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final HorarioFuncionamentoRepository horarioRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final BloqueioRepository bloqueioRepository;

    @Transactional(readOnly = true)
    public List<SlotDisponivelResponse> calcularSlotsDisponiveis(
            String slugBarbearia,
            LocalDate data,
            UUID servicoId,
            UUID barbeiroId) {

        validarData(data);

        Barbearia barbearia = barbeariaRepository.findBySlug(slugBarbearia)
                .orElseThrow(() -> new EntityNotFoundException("Barbearia", slugBarbearia));

        Servico servico = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new EntityNotFoundException("Serviço", servicoId));

        if (!servico.getAtivo() || !servico.getBarbearia().getId().equals(barbearia.getId())) {
            throw new EntityNotFoundException("Serviço", servicoId);
        }

        // Converte DayOfWeek do Java (1=Seg...7=Dom) para convenção do banco (0=Dom, 1=Seg...6=Sab)
        int diaSemana = data.getDayOfWeek().getValue() % 7;

        HorarioFuncionamento horario = horarioRepository
                .findByBarbeariaIdAndDiaSemana(barbearia.getId(), diaSemana)
                .orElse(null);

        if (horario == null) {
            return List.of();
        }

        List<Barbeiro> barbeirosElegiveis = resolverBarbeiros(barbearia, barbeiroId);
        if (barbeirosElegiveis.isEmpty()) {
            return List.of();
        }

        return gerarSlotsDisponiveis(data, horario, servico, barbeirosElegiveis);
    }

    private void validarData(LocalDate data) {
        LocalDate hoje = LocalDate.now();
        if (data.isBefore(hoje)) {
            throw new IllegalArgumentException("Data não pode ser no passado.");
        }
        if (data.isAfter(hoje.plusDays(60))) {
            throw new IllegalArgumentException("Data não pode ser mais de 60 dias no futuro.");
        }
    }

    private List<Barbeiro> resolverBarbeiros(Barbearia barbearia, UUID barbeiroId) {
        if (barbeiroId != null) {
            // findByIdAndBarbeariaIdAndAtivoTrue valida ownership + ativo em 1 query, sem lazy load
            Barbeiro barbeiro = barbeiroRepository
                    .findByIdAndBarbeariaIdAndAtivoTrue(barbeiroId, barbearia.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Barbeiro", barbeiroId));
            return List.of(barbeiro);
        }
        return barbeiroRepository.findByBarbeariaIdAndAtivoTrue(barbearia.getId());
    }

    private List<SlotDisponivelResponse> gerarSlotsDisponiveis(
            LocalDate data,
            HorarioFuncionamento horario,
            Servico servico,
            List<Barbeiro> barbeiros) {

        int duracaoMinutos = servico.getDuracaoMinutos();
        LocalTime abertura = horario.getAbertura();
        LocalTime fechamento = horario.getFechamento();

        // slotFim deve caber dentro do horário de fechamento
        Map<LocalTime, List<BarbeiroResumoResponse>> slotsLivres = new LinkedHashMap<>();

        LocalTime slotInicio = abertura;
        while (!slotInicio.plusMinutes(duracaoMinutos).isAfter(fechamento)) {
            LocalTime slotFim = slotInicio.plusMinutes(duracaoMinutos);

            List<BarbeiroResumoResponse> barbeirosLivres = new ArrayList<>();
            for (Barbeiro barbeiro : barbeiros) {
                if (!estaOcupado(barbeiro.getId(), data, slotInicio, slotFim)) {
                    barbeirosLivres.add(toBarbeiroResumo(barbeiro));
                }
            }

            if (!barbeirosLivres.isEmpty()) {
                slotsLivres.put(slotInicio, barbeirosLivres);
            }

            slotInicio = slotFim;
        }

        return slotsLivres.entrySet().stream()
                .map(e -> new SlotDisponivelResponse(e.getKey(), e.getValue()))
                .toList();
    }

    private boolean estaOcupado(UUID barbeiroId, LocalDate data, LocalTime slotInicio, LocalTime slotFim) {
        return agendamentoRepository.existsConflito(barbeiroId, data, slotInicio, slotFim, STATUS_OCUPADOS)
                || bloqueioRepository.existsBloqueioConflito(barbeiroId, data, slotInicio, slotFim);
    }

    private BarbeiroResumoResponse toBarbeiroResumo(Barbeiro barbeiro) {
        return new BarbeiroResumoResponse(barbeiro.getId(), barbeiro.getNome(), barbeiro.getFotoUrl());
    }
}
