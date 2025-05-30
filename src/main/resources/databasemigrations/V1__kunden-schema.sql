CREATE TABLE Customer (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    gender ENUM('D', 'M', 'U', 'W') NOT NULL
);