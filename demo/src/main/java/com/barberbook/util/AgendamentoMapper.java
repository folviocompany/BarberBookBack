package com.barberbook.util;

import com.barberbook.dto.response.AgendamentoResponse;
import com.barberbook.dto.response.BarbeariaNomeResponse;
import com.barberbook.dto.response.BarbeiroResponse;
import com.barberbook.dto.response.ServicoResponse;
import com.barberbook.entity.Agendamento;

public class AgendamentoMapper {

    private AgendamentoMapper() {}

    public static AgendamentoResponse toResponse(Agendamento a) {
        ServicoResponse servico = new ServicoResponse(
                a.getServico().getId(),
                a.getServico().getNome(),
                a.getServico().getDuracaoMinutos(),
                a.getServico().getPreco()
        );
        BarbeiroResponse barbeiro = new BarbeiroResponse(
                a.getBarbeiro().getId(),
                a.getBarbeiro().getNome(),
                a.getBarbeiro().getFotoUrl()
        );
        BarbeariaNomeResponse barbearia = new BarbeariaNomeResponse(
                a.getBarbearia().getId(),
                a.getBarbearia().getNome(),
                a.getBarbearia().getSlug()
        );
        return new AgendamentoResponse(
                a.getId(), a.getClienteNome(), a.getClienteTelefone(),
                a.getData(), a.getHorario(), a.getStatus(),
                servico, barbeiro, barbearia, a.getCriadoEm()
        );
    }
}
