CREATE TABLE IF NOT EXISTS appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    appointment_date DATETIME NOT NULL,
    service_type VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL
);