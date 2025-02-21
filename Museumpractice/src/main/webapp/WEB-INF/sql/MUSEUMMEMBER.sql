DROP DATABASE IF EXISTS museum_db;

CREATE DATABASE museum_db;

USE museum_db;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       phone VARCHAR(20) NOT NULL UNIQUE,
                       role ENUM('LOCAL', 'FOREIGN', 'ADMIN') NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reservations (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              user_id INT NOT NULL,
                              exhibition VARCHAR(50) NOT NULL CHECK (exhibition IN ('South America', 'Middle East')),
                              reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO users (username, email) VALUES ('test_user', 'test@example.com');

ALTER TABLE users MODIFY COLUMN role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER';

INSERT INTO users (username, password, email, phone, role) VALUES ('yyssjjiinn', 'marksarang', 'sjlovesnct@gmail.com', '010-3926-8125', 'ADMIN');

ALTER TABLE users MODIFY COLUMN role ENUM('LOCAL', 'FOREIGN', 'ADMIN') NOT NULL;

UPDATE users SET role = 'LOCAL' WHERE role = 'USER';

