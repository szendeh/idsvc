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
-- Person-Work join table
CREATE TABLE personworks (
    id SERIAL PRIMARY KEY,
    person_id INT REFERENCES person(id),
    work_id INT REFERENCES work(id)
);
-- Person-Pname join table
CREATE TABLE personpnames (
    id SERIAL PRIMARY KEY,
    person_id INT REFERENCES person(id),
    pname_id INT REFERENCES pname(id)
);
-- Work-Pname join table
CREATE TABLE workpnames (
    id SERIAL PRIMARY KEY,
    work_id INT REFERENCES work(id),
    pname_id INT REFERENCES pname(id)
);