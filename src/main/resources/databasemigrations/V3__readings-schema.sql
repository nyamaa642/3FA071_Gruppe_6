CREATE TABLE readings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,  -- FK zu customers
    property_id INT,  -- FK zu properties
    reading_type ENUM('strom', 'heizung', 'warmwasser', 'wasser', 'gas'),
    value DECIMAL(10, 2),  -- Der gemessene Verbrauchswert
    reading_date DATE,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (property_id) REFERENCES properties(id)
);