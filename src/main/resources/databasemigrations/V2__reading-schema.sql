CREATE TABLE Reading (
    id UUID PRIMARY KEY,
    customer_id UUID,
    type_of_meter ENUM('GAS', 'WATER', 'ELECTRICITY') NOT NULL,
    reading_count DOUBLE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer(id) ON DELETE SET NULL
);