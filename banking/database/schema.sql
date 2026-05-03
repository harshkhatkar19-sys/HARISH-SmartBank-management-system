CREATE DATABASE IF NOT EXISTS harishsmartbank;
USE harishsmartbank;

CREATE TABLE IF NOT EXISTS accounts (
    account_number VARCHAR(20) PRIMARY KEY,
    holder_name VARCHAR(100) NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    balance DOUBLE NOT NULL DEFAULT 0,
    account_type ENUM('SAVINGS', 'CURRENT') NOT NULL,
    aadhaar_number VARCHAR(12),
    phone_number VARCHAR(10),
    father_name VARCHAR(100),
    mother_name VARCHAR(100),
    dob DATE,
    status ENUM('ACTIVE', 'BLOCKED') DEFAULT 'ACTIVE',
    role ENUM('USER', 'ADMIN') DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS cards (
    id INT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(20) UNIQUE NOT NULL,
    account_number VARCHAR(20) NOT NULL,
    holder_name VARCHAR(100) NOT NULL,
    expiry_date VARCHAR(10) NOT NULL,
    cvv VARCHAR(3) NOT NULL,
    pin VARCHAR(4) NOT NULL,
    card_type ENUM('DEBIT', 'CREDIT') NOT NULL,
    status ENUM('ACTIVE', 'BLOCKED') DEFAULT 'ACTIVE',
    FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS loans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    amount DOUBLE NOT NULL,
    interest_rate DOUBLE NOT NULL,
    duration_months INT NOT NULL,
    loan_type VARCHAR(50) NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    applied_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    type VARCHAR(50) NOT NULL,
    amount DOUBLE NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE
);

INSERT INTO accounts (account_number, holder_name, username, password, balance, account_type, status, role)
SELECT 'ADM-001', 'System Administrator', 'admin', 'admin123', 0.0, 'SAVINGS', 'ACTIVE', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'admin');
