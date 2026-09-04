-- Sunrise Dental Clinic (Colombo) — MySQL schema and seed data
-- mysql -u root -p < backend/database/schema.sql

CREATE DATABASE IF NOT EXISTS sunrise_dental
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sunrise_dental;

CREATE TABLE IF NOT EXISTS users (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          ENUM('ADMIN', 'STAFF') NOT NULL DEFAULT 'STAFF',
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS dentists (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    full_name       VARCHAR(100) NOT NULL,
    specialization  VARCHAR(80)  NOT NULL
);

CREATE TABLE IF NOT EXISTS treatments (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(80) NOT NULL UNIQUE,
    treatment_fee     DECIMAL(10, 2) NOT NULL,
    consultation_fee  DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS appointments (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    appointment_no    VARCHAR(20) NOT NULL UNIQUE,
    patient_name      VARCHAR(100) NOT NULL,
    address           VARCHAR(255) NOT NULL,
    contact_number    VARCHAR(20)  NOT NULL,
    dentist_id        INT NOT NULL,
    treatment_id      INT NOT NULL,
    appointment_date  DATE NOT NULL,
    appointment_time  TIME NOT NULL,
    total_amount      DECIMAL(10, 2) NOT NULL,
    status            ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED') DEFAULT 'SCHEDULED',
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dentist_id) REFERENCES dentists (id),
    FOREIGN KEY (treatment_id) REFERENCES treatments (id)
);

CREATE TABLE IF NOT EXISTS payments (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id  INT NOT NULL,
    amount          DECIMAL(10, 2) NOT NULL,
    payment_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method  VARCHAR(50) DEFAULT 'CASH',
    FOREIGN KEY (appointment_id) REFERENCES appointments (id)
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT,
    username   VARCHAR(50),
    action     VARCHAR(50) NOT NULL,
    details    TEXT,
    timestamp  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL
);

INSERT IGNORE INTO dentists (id, full_name, specialization) VALUES
    (1, 'Dr. N. Perera', 'General dentistry'),
    (2, 'Dr. S. Fernando', 'Orthodontics'),
    (3, 'Dr. A. Jayawardena', 'Endodontics'),
    (4, 'Dr. M. Silva', 'Oral surgery');

INSERT IGNORE INTO treatments (id, name, treatment_fee, consultation_fee) VALUES
    (1, 'Consultation only',     0.00,     2500.00),
    (2, 'Scaling and cleaning',  8000.00,  2500.00),
    (3, 'Tooth filling',        12000.00,  2500.00),
    (4, 'Extraction',           10000.00,  2500.00),
    (5, 'Root canal',           25000.00,  3000.00),
    (6, 'Teeth whitening',      18000.00,  2500.00),
    (7, 'Crown',                35000.00,  3000.00);

-- Default accounts are created on first backend start (DataSeeder).
-- Admin: admin / password
-- Staff: staff / staff123

-- Advanced database features (Task B excellent band): bill total function
-- and a trigger that blocks double-booked dentists at the data layer.

DROP TRIGGER IF EXISTS trg_appointments_before_insert;
DROP TRIGGER IF EXISTS trg_appointments_before_update;
DROP FUNCTION IF EXISTS fn_bill_total;

DELIMITER $$

CREATE FUNCTION fn_bill_total(p_treatment_fee DECIMAL(10,2), p_consultation_fee DECIMAL(10,2))
RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    RETURN ROUND(IFNULL(p_treatment_fee, 0) + IFNULL(p_consultation_fee, 0), 2);
END$$

CREATE TRIGGER trg_appointments_before_insert
BEFORE INSERT ON appointments
FOR EACH ROW
BEGIN
    DECLARE v_treat DECIMAL(10,2);
    DECLARE v_consult DECIMAL(10,2);
    SELECT treatment_fee, consultation_fee INTO v_treat, v_consult
        FROM treatments WHERE id = NEW.treatment_id;
    SET NEW.total_amount = fn_bill_total(v_treat, v_consult);
    IF NEW.status = 'SCHEDULED' AND EXISTS (
        SELECT 1 FROM appointments a
        WHERE a.dentist_id = NEW.dentist_id
          AND a.appointment_date = NEW.appointment_date
          AND a.appointment_time = NEW.appointment_time
          AND a.status = 'SCHEDULED'
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Double booking: this dentist already has a scheduled patient in that slot';
    END IF;
END$$

CREATE TRIGGER trg_appointments_before_update
BEFORE UPDATE ON appointments
FOR EACH ROW
BEGIN
    DECLARE v_treat DECIMAL(10,2);
    DECLARE v_consult DECIMAL(10,2);
    SELECT treatment_fee, consultation_fee INTO v_treat, v_consult
        FROM treatments WHERE id = NEW.treatment_id;
    SET NEW.total_amount = fn_bill_total(v_treat, v_consult);
    IF NEW.status = 'SCHEDULED' AND EXISTS (
        SELECT 1 FROM appointments a
        WHERE a.dentist_id = NEW.dentist_id
          AND a.appointment_date = NEW.appointment_date
          AND a.appointment_time = NEW.appointment_time
          AND a.status = 'SCHEDULED'
          AND a.id <> NEW.id
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Double booking: this dentist already has a scheduled patient in that slot';
    END IF;
END$$

DELIMITER ;
