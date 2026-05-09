package com.barberbook.service;

import com.barberbook.dto.response.*;
import com.barberbook.entity.Barbearia;
import com.barberbook.exception.EntityNotFoundException;
import com.barberbook.repository.BarbeariaRepository;
import com.barberbook.repository.BarbeiroRepository;
import com.barberbook.repository.HorarioFuncionamentoRepository;
import com.barberbook.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarbeariaService {

    private final BarbeariaRepository barbeariaRepository;
    private final ServicoRepository servicoRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final HorarioFuncionamentoRepository horarioRepository;

    @Transactional(readOnly = true)
    public BarbeariaPublicaResponse buscarPorSlug(String slug) {
        Barbearia barbearia = buscarBarbeariaOuLancar(slug);

        List<HorarioFuncionamentoResponse> horarios = horarioRepository
                .findByBarbeariaId(barbearia.getId())
                .stream()
                .map(h -> new HorarioFuncionamentoResponse(h.getDiaSemana(), h.getAbertura(), h.getFechamento()))
                .toList();

        return new BarbeariaPublicaResponse(
                barbearia.getId(),
                barbearia.getNome(),
                barbearia.getSlug(),
                barbearia.getTelefone(),
                barbearia.getEndereco(),
                barbearia.getLogoUrl(),
                horarios
        );
    }

    @Transactional(readOnly = true)
    public List<ServicoResponse> listarServicosAtivos(String slug) {
        Barbearia barbearia = buscarBarbeariaOuLancar(slug);

        return servicoRepository.findByBarbeariaIdAndAtivoTrue(barbearia.getId())
                .stream()
                .map(s -> new ServicoResponse(s.getId(), s.getNome(), s.getDuracaoMinutos(), s.getPreco()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BarbeiroResponse> listarBarbeirosAtivos(String slug) {
        Barbearia barbearia = buscarBarbeariaOuLancar(slug);

        return barbeiroRepository.findByBarbeariaIdAndAtivoTrue(barbearia.getId())
                .stream()
                .map(b -> new BarbeiroResponse(b.getId(), b.getNome(), b.getFotoUrl()))
                .toList();
    }

    private Barbearia buscarBarbeariaOuLancar(String slug) {
        return barbeariaRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Barbearia", slug));
    }
}
