INSERT INTO products (type, name, description, price, image_url) VALUES
                                                                     ('fruit', 'Apple', 'Fresh red apples from local orchards', 2.99, 'https://example.com/apple.jpg'),
                                                                     ('fruit', 'Banana', 'Ripe yellow bananas, perfect for smoothies', 1.99, 'https://example.com/banana.jpg'),
                                                                     ('vegetable', 'Carrot', 'Organic carrots, great for cooking and snacking', 1.49, 'https://example.com/carrot.jpg'),
                                                                     ('vegetable', 'Broccoli', 'Fresh green broccoli, rich in vitamins', 3.49, 'https://example.com/broccoli.jpg'),
                                                                     ('dairy', 'Milk', 'Fresh whole milk, 1 gallon', 4.99, 'https://example.com/milk.jpg'),
                                                                     ('bakery', 'Bread', 'Whole wheat bread, freshly baked', 2.49, 'https://example.com/bread.jpg');

-- ===================================================
-- Database Schema (MySQL)
-- Run this in your MySQL database to create the table

CREATE DATABASE IF NOT EXISTS store_db;
USE store_db;

CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          type VARCHAR(50) NOT NULL,
                          name VARCHAR(100) NOT NULL,
                          description TEXT,
                          price DECIMAL(10, 2) NOT NULL,
                          image_url TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);