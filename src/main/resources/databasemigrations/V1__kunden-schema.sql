CREATE TABLE customer (
    id uuid NOT NULL,
    firstname character varying(100) NOT NULL,
    lastname character varying(100) NOT NULL,
    birthDate date NOT NULL,
    primary key(id)
)