CREATE TABLE meters (
    id INT AUTO_INCREMENT PRIMARY KEY,
    meter_number VARCHAR(50),
    meter_type ENUM('strom', 'heizung', 'warmwasser', 'wasser', 'gas'),
    property_id INT,  -- FK zu properties
    installation_date DATE,
    FOREIGN KEY (property_id) REFERENCES properties(id)
);