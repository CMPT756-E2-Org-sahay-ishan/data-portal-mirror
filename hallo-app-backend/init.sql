/* Create table and pre-populate roles */
CREATE TABLE IF NOT EXISTS roles(
   id serial PRIMARY KEY,
   authority VARCHAR (255) UNIQUE NOT NULL
);

INSERT INTO roles (authority)
VALUES
    ('ROLE_ADMIN'),
    ('ROLE_RESEARCHER'),
    ('ROLE_ANNOTATER'),
    ('ROLE_PUBLIC')
RETURNING *;