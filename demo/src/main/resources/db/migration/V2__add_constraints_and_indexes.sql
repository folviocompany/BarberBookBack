-- BarberBook – constraints e índices de performance

-- Garante que não existam duas barbearias com o mesmo e-mail
ALTER TABLE barbearias
    ADD CONSTRAINT uq_barbearias_email UNIQUE (email);

-- Impede status inválidos de entrarem diretamente no banco
ALTER TABLE agendamentos
    ADD CONSTRAINT chk_agendamentos_status
    CHECK (status IN ('PENDENTE', 'CONFIRMADO', 'CANCELADO', 'CONCLUIDO'));

-- Índice composto usado por existsBloqueioConflito e findFiltrado (barbeiro + data)
CREATE INDEX idx_bloqueios_barbeiro_data
    ON bloqueios(barbeiro_id, data);

-- Índice composto usado por findByBarbeariaIdAndDiaSemana (chamado em cada cálculo de slots)
CREATE INDEX idx_horarios_barbearia_dia
    ON horarios_funcionamento(barbearia_id, dia_semana);
