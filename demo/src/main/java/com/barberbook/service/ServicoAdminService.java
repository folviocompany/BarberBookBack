package com.barberbook.service;

import com.barberbook.dto.request.AtualizarServicoRequest;
import com.barberbook.dto.request.CriarServicoRequest;
import com.barberbook.dto.response.ServicoAdminResponse;
import com.barberbook.entity.Barbearia;
import com.barberbook.entity.Servico;
import com.barberbook.exception.EntityNotFoundException;
import com.barberbook.repository.BarbeariaRepository;
import com.barberbook.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServicoAdminService {

    private final ServicoRepository servicoRepository;
    private final BarbeariaRepository barbeariaRepository;

    public List<ServicoAdminResponse> listar(UUID barbeariaId) {
        return servicoRepository.findByBarbeariaId(barbeariaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ServicoAdminResponse criar(UUID barbeariaId, CriarServicoRequest request) {
        Barbearia barbearia = barbeariaRepository.findById(barbeariaId)
                .orElseThrow(() -> new EntityNotFoundException("Barbearia não encontrada"));

        Servico servico = Servico.builder()
                .barbearia(barbearia)
                .nome(request.nome())
                .duracaoMinutos(request.duracaoMinutos())
                .preco(request.preco())
                .ativo(true)
                .build();

        return toResponse(servicoRepository.save(servico));
    }

    @Transactional
    public ServicoAdminResponse atualizar(UUID barbeariaId, UUID servicoId, AtualizarServicoRequest request) {
        Servico servico = buscarOuLancar(barbeariaId, servicoId);
        servico.setNome(request.nome());
        servico.setDuracaoMinutos(request.duracaoMinutos());
        servico.setPreco(request.preco());
        return toResponse(servicoRepository.save(servico));
    }

    @Transactional
    public ServicoAdminResponse alternarAtivo(UUID barbeariaId, UUID servicoId) {
        Servico servico = buscarOuLancar(barbeariaId, servicoId);
        servico.setAtivo(!servico.getAtivo());
        return toResponse(servicoRepository.save(servico));
    }

    private Servico buscarOuLancar(UUID barbeariaId, UUID servicoId) {
        return servicoRepository.findByIdAndBarbeariaId(servicoId, barbeariaId)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado"));
    }

    private ServicoAdminResponse toResponse(Servico s) {
        return new ServicoAdminResponse(s.getId(), s.getNome(), s.getDuracaoMinutos(), s.getPreco(), s.getAtivo());
    }
}
