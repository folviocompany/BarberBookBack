package com.barberbook.service;

import com.barberbook.dto.request.AtualizarBarbeariaRequest;
import com.barberbook.dto.request.HorarioFuncionamentoRequest;
import com.barberbook.dto.request.LogoRequest;
import com.barberbook.dto.response.BarbeariaAdminResponse;
import com.barberbook.dto.response.HorarioFuncionamentoResponse;
import com.barberbook.entity.Barbearia;
import com.barberbook.entity.HorarioFuncionamento;
import com.barberbook.exception.EntityNotFoundException;
import com.barberbook.repository.BarbeariaRepository;
import com.barberbook.repository.HorarioFuncionamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BarbeariaAdminService {

    private final BarbeariaRepository barbeariaRepository;
    private final HorarioFuncionamentoRepository horarioRepository;

    public BarbeariaAdminResponse buscar(UUID barbeariaId) {
        Barbearia b = buscarOuLancar(barbeariaId);
        return toResponse(b);
    }

    @Transactional
    public BarbeariaAdminResponse atualizar(UUID barbeariaId, AtualizarBarbeariaRequest request) {
        Barbearia b = buscarOuLancar(barbeariaId);
        b.setNome(request.nome());
        b.setTelefone(request.telefone());
        b.setEndereco(request.endereco());
        return toResponse(barbeariaRepository.save(b));
    }

    @Transactional
    public BarbeariaAdminResponse atualizarHorarios(UUID barbeariaId, List<HorarioFuncionamentoRequest> horarios) {
        Barbearia b = buscarOuLancar(barbeariaId);
        horarioRepository.deleteByBarbeariaId(barbeariaId);

        List<HorarioFuncionamento> novos = horarios.stream()
                .map(h -> HorarioFuncionamento.builder()
                        .barbearia(b)
                        .diaSemana(h.diaSemana())
                        .abertura(h.abertura())
                        .fechamento(h.fechamento())
                        .build())
                .toList();

        List<HorarioFuncionamento> salvos = horarioRepository.saveAll(novos);
        // Usa `b` e `salvos` que já estão em memória — sem re-fetch ao banco
        return buildResponse(b, salvos);
    }

    @Transactional
    public BarbeariaAdminResponse atualizarLogo(UUID barbeariaId, LogoRequest request) {
        Barbearia b = buscarOuLancar(barbeariaId);
        b.setLogoUrl(request.logoUrl());
        return toResponse(barbeariaRepository.save(b));
    }

    private Barbearia buscarOuLancar(UUID id) {
        return barbeariaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Barbearia não encontrada"));
    }

    private BarbeariaAdminResponse toResponse(Barbearia b) {
        List<HorarioFuncionamento> horarios = horarioRepository.findByBarbeariaId(b.getId());
        return buildResponse(b, horarios);
    }

    private BarbeariaAdminResponse buildResponse(Barbearia b, List<HorarioFuncionamento> horarios) {
        List<HorarioFuncionamentoResponse> horariosDto = horarios.stream()
                .map(h -> new HorarioFuncionamentoResponse(h.getDiaSemana(), h.getAbertura(), h.getFechamento()))
                .toList();
        return new BarbeariaAdminResponse(
                b.getId(), b.getNome(), b.getSlug(), b.getTelefone(),
                b.getEndereco(), b.getLogoUrl(), b.getEmail(), b.getStatus(), horariosDto
        );
    }
}
