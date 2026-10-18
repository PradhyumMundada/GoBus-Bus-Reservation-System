USE bus_reservation;

-- Run this after demonstrations to return the supplied sample data to a clean state.
DELETE FROM bookings;
UPDATE buses SET available_seats = total_seats;
