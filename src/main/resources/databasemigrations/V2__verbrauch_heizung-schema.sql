CREATE TABLE verbrauch_heizung (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Kunde CHAR(36) NOT NULL,
    Zaehlernummer VARCHAR(50),
    Datum DATE NOT NULL,
    Zaehlerstand_MWh DECIMAL(10, 3),
    Kommentar VARCHAR(255),
    FOREIGN KEY (Kunde) REFERENCES kunden(UUID) ON DELETE CASCADE
);