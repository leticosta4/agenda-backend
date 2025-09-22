
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS agenda (
    "id" UUID NOT NULL DEFAULT uuid_generate_v4(),
    "nome" VARCHAR(50) NOT NULL,
    PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS contato (
    "id" UUID NOT NULL DEFAULT uuid_generate_v4(),
    "agenda_id" UUID DEFAULT NULL,
    "nome" VARCHAR(50) NOT NULL,
    "telefone" VARCHAR(11) NOT NULL UNIQUE,
    "criado_em" TIMESTAMP DEFAULT now(),
    "modificado_em" TIMESTAMP DEFAULT NULL,
    PRIMARY KEY("id"),
    CONSTRAINT "FK_agenda_id" FOREIGN KEY ("agenda_id") REFERENCES "agenda"("id") ON UPDATE CASCADE ON DELETE SET NULL
);


-- INSERT INTO agenda (nome) VALUES
-- ('Agenda de Fulano');


-- INSERT INTO contato (agenda_id, nome, telefone) VALUES
-- ((SELECT id FROM agenda WHERE nome='Agenda de Fulano'), 'Let', '71999999999'),
-- ((SELECT id FROM agenda WHERE nome='Agenda de Fulano'), 'Alysson', '71988888888'),
-- ((SELECT id FROM agenda WHERE nome='Agenda de Fulano'), 'Cainan', '71977777777');
