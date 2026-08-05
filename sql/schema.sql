-- ============================================
-- Restaurant Management System - Module 1
-- Database Script
-- ============================================

CREATE DATABASE IF NOT EXISTS restaurant_db;

USE restaurant_db;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Password for 'admin123' encoded using BCrypt
INSERT INTO users (username, password, role)
VALUES (
    'admin',
    '$2a$10$7EqJtq98hPqEX7fNZaFWoOa8Vt4o4vXqZLxUqhWU6MB3zC6XU7dJK',
    'ADMIN'
);