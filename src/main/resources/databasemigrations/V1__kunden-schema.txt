CREATE TABLE kunden (
    UUID CHAR(36) NOT NULL PRIMARY KEY,
    Anrede VARCHAR(10),
    Vorname VARCHAR(50),
    Nachname VARCHAR(50),
    Geburtsdatum DATE
);