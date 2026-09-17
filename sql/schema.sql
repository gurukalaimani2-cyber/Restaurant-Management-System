-- ============================================
-- Restaurant Management System
-- Database Script (all modules: Users, Reservations,
-- Menu, Orders, Billing, Inventory, Employees)
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

-- ---------- Module 5: Billing ----------
DROP TABLE IF EXISTS bills;

CREATE TABLE bills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    tax_percent DECIMAL(5,2) NOT NULL,
    discount DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_status VARCHAR(20) NOT NULL,
    payment_method VARCHAR(30),
    billed_at DATETIME NOT NULL
);

-- ---------- Module 7: Inventory ----------
DROP TABLE IF EXISTS inventory_items;

CREATE TABLE inventory_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    quantity DECIMAL(10,2) NOT NULL,
    reorder_level DECIMAL(10,2) NOT NULL
);

INSERT INTO inventory_items (item_name, unit, quantity, reorder_level) VALUES
('Rice', 'kg', 50.00, 10.00),
('Chicken', 'kg', 8.00, 10.00),
('Milk', 'litre', 20.00, 5.00),
('Cooking Oil', 'litre', 3.00, 5.00);

-- ---------- Module 8: Employees ----------
DROP TABLE IF EXISTS employee_attendance;
DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    position VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    salary DECIMAL(10,2) NOT NULL,
    joining_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    username VARCHAR(50) UNIQUE -- links to `users` for staff portal login; NULL = no login yet
);

-- ---------- Module 9: Employee Attendance (staff portal) ----------
CREATE TABLE employee_attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    work_date DATE NOT NULL,
    clock_in DATETIME NOT NULL,
    clock_out DATETIME NULL,
    CONSTRAINT fk_attendance_employee FOREIGN KEY (employee_id) REFERENCES employees(id)
);
