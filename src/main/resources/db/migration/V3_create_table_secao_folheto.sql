CREATE TABLE secao_folheto (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255),
    conteudo TEXT,
    ordem INTEGER,
    tipo VARCHAR(50),
    folheto_id BIGINT,
    CONSTRAINT fk_secao_folheto_id FOREIGN KEY (folheto_id) REFERENCES folhetos(id)