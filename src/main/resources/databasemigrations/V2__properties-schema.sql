CREATE TABLE properties (
    id INT AUTO_INCREMENT PRIMARY KEY,
    address VARCHAR(255),
    unit_number VARCHAR(50),  -- z.B. Wohnung 1A
    owner_id INT,  -- FK zu customers (Eigentümer)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES customers(id)
);