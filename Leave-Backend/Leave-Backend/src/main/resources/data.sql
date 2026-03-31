-- Passwords are "password" encoded with BCrypt
-- Admin User
INSERT IGNORE INTO users (id, first_name, last_name, email, password, role, department, created_at) VALUES 
(1, 'Admin', 'User', 'admin@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.9.6UXm6', 'ADMIN', 'IT', NOW());

-- Manager User
INSERT IGNORE INTO users (id, first_name, last_name, email, password, role, department, created_at) VALUES 
(2, 'Manager', 'User', 'manager@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.9.6UXm6', 'MANAGER', 'HR', NOW());

-- Employee Users
INSERT IGNORE INTO users (id, first_name, last_name, email, password, role, department, created_at) VALUES 
(3, 'Employee', 'One', 'emp1@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.9.6UXm6', 'EMPLOYEE', 'Engineering', NOW()),
(4, 'Employee', 'Two', 'emp2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.9.6UXm6', 'EMPLOYEE', 'Engineering', NOW());

-- Leave Balances
INSERT IGNORE INTO leave_balances (id, user_id, annual_leave, sick_leave, casual_leave, year) VALUES 
(1, 1, 12, 10, 6, 2024),
(2, 2, 12, 10, 6, 2024),
(3, 3, 12, 10, 6, 2024),
(4, 4, 12, 10, 6, 2024);
