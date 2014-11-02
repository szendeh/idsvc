-- Database for idsvc in PostgreSQL DDL dialect
-- Person table
CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    label VARCHAR(128),
    created TIMESTAMP
);
-- Pident (personal identifier) table
CREATE TABLE pident (
    id SERIAL PRIMARY KEY,
    person_id INT REFERENCES person(id),
    schema VARCHAR(12) NOT NULL,
    identifier VARCHAR(128) NOT NULL
);
-- Work table
CREATE TABLE work (
    id SERIAL PRIMARY KEY,
    schema VARCHAR(12) NOT NULL,
    identifier VARCHAR(128) NOT NULL
);
-- Pname (personal name) table
CREATE TABLE pname (
    id SERIAL PRIMARY KEY,
    pname VARCHAR(128) NOT NULL
);
-- Claim table
CREATE TABLE claim (
    id SERIAL PRIMARY KEY,
    created TIMESTAMP,
    source VARCHAR(12),
    pident_id INT REFERENCES pident(id),
    work_id INT REFERENCES work(id),
    pname_id INT REFERENCES pname(id)
);
