--liquibase formatted sql

--changeset dmshed:1
CREATE TABLE IF NOT EXISTS revision
(
    id SERIAL PRIMARY KEY ,
    timestamp BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS users_aud
(
    id BIGINT,
    rev INT REFERENCES revision (id),
    revtype SMALLINT,
    username VARCHAR(64) ,
    birth_date DATE,
    firstname VARCHAR(64),
    lastname VARCHAR(64),
    role VARCHAR(32),
    company_id INT
);