--liquibase formatted sql

--changeset dmshed:1
ALTER TABLE users
    ADD COLUMN create_at TIMESTAMP;

ALTER TABLE users
    ADD COLUMN modified_at TIMESTAMP;

ALTER TABLE users
    ADD COLUMN create_by VARCHAR(32);

ALTER TABLE users
    ADD COLUMN modified_by VARCHAR(32);