package com.barberbook.service;

import com.barberbook.dto.request.CriarBloqueioRequest;
import com.barberbook.dto.response.BloqueioResponse;
import com.barberbook.entity.Barbeiro;
import com.barberbook.entity.Bloqueio;
import com.barberbook.exception.ConflictException;
import com.barberbook.exception.EntityNotFoundException;
import com.barberbook.repository.BarbeiroRepository;
import com.barberbook.repository.BloqueioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BloqueioAdminService {

    private final BloqueioRepository bloqueioRepository;
    private final BarbeiroRepository barbeiroRepository;

    public List<BloqueioResponse> listar(UUID barbeariaId, UUID barbeiroId, LocalDate data) {
        return bloqueioRepository.findFiltrado(barbeariaId, barbeiroId, data)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public BloqueioResponse criar(UUID barbeariaId, CriarBloqueioRequest request) {
        if (!request.inicio().isBefore(request.fim())) {
            throw new ConflictException("O horário de início deve ser anterior ao fim");
        }

        Barbeiro barbeiro = barbeiroRepository.findByIdAndBarbeariaId(request.barbeiroId(), barbeariaId)
                .orElseThrow(() -> new EntityNotFoundException("Barbeiro não encontrado"));

        Bloqueio bloqueio = Bloqueio.builder()
                .barbeiro(barbeiro)
                .data(request.data())
                .inicio(request.inicio())
                .fim(request.fim())
                .motivo(request.motivo())
                .build();

        return toResponse(bloqueioRepository.save(bloqueio));
    }

    @Transactional
    public void deletar(UUID barbeariaId, UUID bloqueioId) {
        // findByIdAndBarbeariaId faz JOIN em uma query só — evita cadeia Bloqueio→Barbeiro→Barbearia
        bloqueioRepository.findByIdAndBarbeariaId(bloqueioId, barbeariaId)
                .orElseThrow(() -> new EntityNotFoundException("Bloqueio não encontrado"));

        bloqueioRepository.deleteById(bloqueioId);
    }

    private BloqueioResponse toResponse(Bloqueio b) {
        return new BloqueioResponse(
                b.getId(),
                b.getBarbeiro().getId(),
                b.getBarbeiro().getNome(),
                b.getData(),
                b.getInicio(),
                b.getFim(),
                b.getMotivo()
        );
    }
}
