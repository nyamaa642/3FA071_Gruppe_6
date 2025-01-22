CREATE TABLE Reading (
    id UUID PRIMARY KEY,
    comment TEXT,
    customer_id UUID,
    date_of_reading DATE NOT NULL,
    kind_of_meter ENUM('HEIZUNG', 'STROM', 'WASSER', 'UNBEKANNT') NOT NULL,
    meter_count DOUBLE NOT NULL,
    meter_id VARCHAR(255) NOT NULL,
    substitute BOOLEAN NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer(id) ON DELETE SET NULL
);