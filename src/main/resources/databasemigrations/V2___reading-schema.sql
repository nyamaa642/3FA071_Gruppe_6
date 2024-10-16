CREATE TABLE reading (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Kunde CHAR(36) NOT NULL,              -- Verknüpfung zu 'customer'
    Zaehlernummer VARCHAR(50),            -- Zählernummer (einheitlich)
    Datum DATE NOT NULL,                  -- Datum der Ablesung
    Zaehlerstand DECIMAL(10, 3),          -- Ablesewert (kWh, MWh, m3)
    Einheit VARCHAR(10),                  -- Einheit (kWh, MWh, m³)
    Typ VARCHAR(20),                      -- Typ (Heizung, Strom, Wasser)
    Kommentar VARCHAR(255),               -- Optionaler Kommentar
    FOREIGN KEY (Kunde) REFERENCES customer(UUID) ON DELETE CASCADE
);