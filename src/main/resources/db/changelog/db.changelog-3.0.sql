--liquibase formatted sql

--changeset dmshed:1
ALTER TABLE users
ADD COLUMN image VARCHAR(64);

--changeset dmshed:2
ALTER TABLE users_aud
ADD COLUMN image VARCHAR(64);
