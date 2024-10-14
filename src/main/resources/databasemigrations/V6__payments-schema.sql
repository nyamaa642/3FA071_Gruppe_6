CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,  -- FK zu customers
    bill_id INT,  -- FK zu bills
    payment_amount DECIMAL(10, 2),
    payment_date DATE,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (bill_id) REFERENCES bills(id)
);