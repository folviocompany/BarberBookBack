package com.barberbook.service;

import com.barberbook.dto.request.AtualizarBarbeiroRequest;
import com.barberbook.dto.request.CriarBarbeiroRequest;
import com.barberbook.dto.response.BarbeiroAdminResponse;
import com.barberbook.entity.Barbeiro;
import com.barberbook.entity.Barbearia;
import com.barberbook.exception.EntityNotFoundException;
import com.barberbook.repository.BarbeariaRepository;
import com.barberbook.repository.BarbeiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BarbeiroAdminService {

    private final BarbeiroRepository barbeiroRepository;
    private final BarbeariaRepository barbeariaRepository;

    public List<BarbeiroAdminResponse> listar(UUID barbeariaId) {
        return barbeiroRepository.findByBarbeariaId(barbeariaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public BarbeiroAdminResponse criar(UUID barbeariaId, CriarBarbeiroRequest request) {
        Barbearia barbearia = barbeariaRepository.findById(barbeariaId)
                .orElseThrow(() -> new EntityNotFoundException("Barbearia não encontrada"));

        Barbeiro barbeiro = Barbeiro.builder()
                .barbearia(barbearia)
                .nome(request.nome())
                .fotoUrl(request.fotoUrl())
                .ativo(true)
                .build();

        return toResponse(barbeiroRepository.save(barbeiro));
    }

    @Transactional
    public BarbeiroAdminResponse atualizar(UUID barbeariaId, UUID barbeiroId, AtualizarBarbeiroRequest request) {
        Barbeiro barbeiro = buscarOuLancar(barbeariaId, barbeiroId);
        barbeiro.setNome(request.nome());
        barbeiro.setFotoUrl(request.fotoUrl());
        return toResponse(barbeiroRepository.save(barbeiro));
    }

    @Transactional
    public BarbeiroAdminResponse alternarAtivo(UUID barbeariaId, UUID barbeiroId) {
        Barbeiro barbeiro = buscarOuLancar(barbeariaId, barbeiroId);
        barbeiro.setAtivo(!barbeiro.getAtivo());
        return toResponse(barbeiroRepository.save(barbeiro));
    }

    private Barbeiro buscarOuLancar(UUID barbeariaId, UUID barbeiroId) {
        return barbeiroRepository.findByIdAndBarbeariaId(barbeiroId, barbeariaId)
                .orElseThrow(() -> new EntityNotFoundException("Barbeiro não encontrado"));
    }

    private BarbeiroAdminResponse toResponse(Barbeiro b) {
        return new BarbeiroAdminResponse(b.getId(), b.getNome(), b.getFotoUrl(), b.getAtivo());
    }
}
