-- ============================================
-- Restaurant Management System
-- Database Script (Module 1 + Module 2 + Module 3 + Module 4)
-- ============================================

CREATE DATABASE IF NOT EXISTS restaurant_db;

USE restaurant_db;

-- ---------- Module 1: Users ----------
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Password for 'admin123' encoded using BCrypt (verified working hash)
INSERT INTO users (username, password, role)
VALUES (
    'admin',
    '$2a$12$u4oW.7zz8lt0z1rjM7AsguKfaV8qbdKnRbSTn7D9fy8ISmUGESSJe',
    'ADMIN'
);

-- ---------- Module 2: Reservations ----------
DROP TABLE IF EXISTS reservations;

CREATE TABLE reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    reservation_date DATE NOT NULL,
    reservation_time TIME NOT NULL,
    number_of_guests INT NOT NULL,
    table_number INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);

-- ---------- Module 3: Menu Items ----------
DROP TABLE IF EXISTS menu_items;

CREATE TABLE menu_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description VARCHAR(255),
    available BOOLEAN NOT NULL DEFAULT TRUE
);

-- Sample menu data (optional, remove if you don't want seed data)
INSERT INTO menu_items (item_name, category, price, description, available) VALUES
('Paneer Tikka', 'Starter', 220.00, 'Grilled cottage cheese with spices', TRUE),
('Butter Chicken', 'Main Course', 350.00, 'Creamy tomato chicken curry', TRUE),
('Gulab Jamun', 'Dessert', 90.00, 'Sweet milk dumplings in syrup', TRUE),
('Masala Chai', 'Beverage', 40.00, 'Spiced Indian tea', TRUE);

-- ---------- Module 4: Orders ----------
DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_number INT NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    items_ordered VARCHAR(500) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    order_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    order_time DATETIME NOT NULL
);
