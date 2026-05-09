-- BarberBook – schema inicial

CREATE TABLE barbearias (
    id           UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    nome         VARCHAR(255) NOT NULL,
    slug         VARCHAR(100) NOT NULL UNIQUE,
    telefone     VARCHAR(20),
    endereco     TEXT,
    logo_url     TEXT,
    email        VARCHAR(255) NOT NULL,
    senha_hash   VARCHAR(255) NOT NULL,
    status       VARCHAR(20)  NOT NULL DEFAULT 'ATIVO',
    criado_em    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE barbeiros (
    id            UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    barbearia_id  UUID         NOT NULL REFERENCES barbearias(id) ON DELETE CASCADE,
    nome          VARCHAR(255) NOT NULL,
    foto_url      TEXT,
    ativo         BOOLEAN      NOT NULL DEFAULT true
);

CREATE TABLE servicos (
    id               UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    barbearia_id     UUID           NOT NULL REFERENCES barbearias(id) ON DELETE CASCADE,
    nome             VARCHAR(255)   NOT NULL,
    duracao_minutos  INT            NOT NULL,
    preco            NUMERIC(10, 2) NOT NULL,
    ativo            BOOLEAN        NOT NULL DEFAULT true
);

CREATE TABLE horarios_funcionamento (
    id            UUID    PRIMARY KEY DEFAULT gen_random_uuid(),
    barbearia_id  UUID    NOT NULL REFERENCES barbearias(id) ON DELETE CASCADE,
    dia_semana    INT     NOT NULL CHECK (dia_semana BETWEEN 0 AND 6),
    abertura      TIME    NOT NULL,
    fechamento    TIME    NOT NULL
);

CREATE TABLE agendamentos (
    id                UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    barbearia_id      UUID         NOT NULL REFERENCES barbearias(id),
    barbeiro_id       UUID         NOT NULL REFERENCES barbeiros(id),
    servico_id        UUID         NOT NULL REFERENCES servicos(id),
    cliente_nome      VARCHAR(255) NOT NULL,
    cliente_telefone  VARCHAR(20),
    data              DATE         NOT NULL,
    horario           TIME         NOT NULL,
    status            VARCHAR(20)  NOT NULL DEFAULT 'PENDENTE',
    criado_em         TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE bloqueios (
    id           UUID  PRIMARY KEY DEFAULT gen_random_uuid(),
    barbeiro_id  UUID  NOT NULL REFERENCES barbeiros(id) ON DELETE CASCADE,
    data         DATE  NOT NULL,
    inicio       TIME  NOT NULL,
    fim          TIME  NOT NULL,
    motivo       TEXT
);

-- Índices
CREATE INDEX idx_agendamentos_barbearia_data ON agendamentos(barbearia_id, data);
CREATE INDEX idx_agendamentos_barbeiro_data  ON agendamentos(barbeiro_id, data);
CREATE INDEX idx_barbearias_slug             ON barbearias(slug);
