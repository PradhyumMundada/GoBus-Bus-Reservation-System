CREATE DATABASE IF NOT EXISTS bus_reservation;
USE bus_reservation;

CREATE TABLE IF NOT EXISTS buses (
    bus_id INT PRIMARY KEY AUTO_INCREMENT,
    bus_number VARCHAR(20) NOT NULL UNIQUE,
    bus_name VARCHAR(100) NOT NULL,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL,
    fare DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    bus_id INT NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    passenger_age INT NOT NULL,
    passenger_gender VARCHAR(10) NOT NULL,
    seats_booked INT NOT NULL,
    total_fare DECIMAL(10,2) NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bus_id) REFERENCES buses(bus_id)
);

INSERT IGNORE INTO buses (bus_number, bus_name, source, destination, departure_time, arrival_time, total_seats, available_seats, fare) VALUES
('MH12AB1234', 'Shivneri Express', 'Pune', 'Mumbai', '08:00:00', '11:30:00', 40, 40, 450.00),
('MH14CD5678', 'City Express', 'Pune', 'Nashik', '09:30:00', '14:00:00', 40, 40, 550.00),
('RJ01EF9012', 'Royal Travels', 'Ajmer', 'Jaipur', '07:00:00', '10:00:00', 45, 45, 350.00),
('MH20AB1234', 'Shivneri Travels', 'Mumbai', 'Delhi', '08:00:00', '20:00:00', 40, 40, 1200.00);
