CREATE TABLE bills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,  -- FK zu customers
    property_id INT,  -- FK zu properties
    bill_amount DECIMAL(10, 2),  -- Betrag der Rechnung
    bill_date DATE,
    due_date DATE,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (property_id) REFERENCES properties(id)
);