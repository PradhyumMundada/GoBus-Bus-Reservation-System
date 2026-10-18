package com.busreservation.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/** Booking operations use transactions so bookings and seat counts never disagree. */
public class BookingDAO {
    private int lastBookingId = -1;

    public int getLastBookingId() { return lastBookingId; }

    public boolean bookTicket(int busId, String passengerName, int passengerAge, String passengerGender, int seatsBooked) {
        lastBookingId = -1;
        if (passengerName == null || passengerName.trim().isEmpty() || passengerAge < 1 || passengerAge > 120
                || seatsBooked <= 0 || !isValidGender(passengerGender)) return false;
        String busSql = "SELECT fare, available_seats FROM buses WHERE bus_id = ? FOR UPDATE";
        String insertSql = "INSERT INTO bookings (bus_id, passenger_name, passenger_age, passenger_gender, seats_booked, total_fare) VALUES (?, ?, ?, ?, ?, ?)";
        String updateSql = "UPDATE buses SET available_seats = available_seats - ? WHERE bus_id = ?";
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement busStatement = connection.prepareStatement(busSql)) {
                    busStatement.setInt(1, busId);
                    try (ResultSet result = busStatement.executeQuery()) {
                        if (!result.next() || seatsBooked > result.getInt("available_seats")) {
                            connection.rollback();
                            return false;
                        }
                        double totalFare = result.getDouble("fare") * seatsBooked;
                        try (PreparedStatement insert = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                             PreparedStatement update = connection.prepareStatement(updateSql)) {
                            insert.setInt(1, busId); insert.setString(2, passengerName.trim()); insert.setInt(3, passengerAge);
                            insert.setString(4, passengerGender); insert.setInt(5, seatsBooked); insert.setDouble(6, totalFare);
                            if (insert.executeUpdate() != 1) throw new SQLException("Booking was not inserted.");
                            try (ResultSet keys = insert.getGeneratedKeys()) {
                                if (!keys.next()) throw new SQLException("Booking ID was not generated.");
                                lastBookingId = keys.getInt(1);
                            }
                            update.setInt(1, seatsBooked); update.setInt(2, busId);
                            if (update.executeUpdate() != 1) throw new SQLException("Seat count was not updated.");
                        }
                    }
                }
                connection.commit();
                return true;
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            }
        } catch (SQLException exception) {
            System.err.println("Booking failed: " + exception.getMessage());
            return false;
        }
    }

    public String viewBooking(int bookingId) {
        String sql = "SELECT b.booking_id, b.bus_id, bus.bus_name, bus.source, bus.destination, bus.departure_time, bus.arrival_time, b.passenger_name, b.passenger_age, b.passenger_gender, b.seats_booked, b.total_fare, b.booking_date FROM bookings b JOIN buses bus ON b.bus_id = bus.bus_id WHERE b.booking_id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookingId);
            try (ResultSet row = statement.executeQuery()) {
                if (!row.next()) return "No booking found for Booking ID: " + bookingId;
                return String.format("BOOKING DETAILS%n%nBooking ID: %d%nBus ID: %d%nBus Name: %s%nRoute: %s to %s%nDeparture: %s%nArrival: %s%n%nPassenger Name: %s%nPassenger Age: %d%nGender: %s%nSeats Booked: %d%nTotal Fare: Rs. %.2f%nBooking Date: %s",
                        row.getInt("booking_id"), row.getInt("bus_id"), row.getString("bus_name"), row.getString("source"), row.getString("destination"),
                        row.getTime("departure_time"), row.getTime("arrival_time"), row.getString("passenger_name"), row.getInt("passenger_age"),
                        row.getString("passenger_gender"), row.getInt("seats_booked"), row.getDouble("total_fare"), row.getTimestamp("booking_date"));
            }
        } catch (SQLException exception) {
            System.err.println("Could not view booking: " + exception.getMessage());
            return "Unable to read booking. Check the database connection.";
        }
    }

    public ArrayList<Object[]> getAllBookingsForGUI() {
        ArrayList<Object[]> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.passenger_name, b.passenger_age, b.passenger_gender, bus.bus_name, bus.source, bus.destination, b.seats_booked, b.total_fare, b.booking_date FROM bookings b JOIN buses bus ON b.bus_id = bus.bus_id ORDER BY b.booking_id";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet row = statement.executeQuery()) {
            while (row.next()) bookings.add(new Object[]{row.getInt("booking_id"), row.getString("passenger_name"), row.getInt("passenger_age"), row.getString("passenger_gender"), row.getString("bus_name"), row.getString("source"), row.getString("destination"), row.getInt("seats_booked"), row.getDouble("total_fare"), row.getTimestamp("booking_date")});
        } catch (SQLException exception) { System.err.println("Could not read bookings: " + exception.getMessage()); }
        return bookings;
    }

    public void viewAllBookings() {
        for (Object[] booking : getAllBookingsForGUI()) System.out.println(java.util.Arrays.toString(booking));
    }

    public boolean cancelBooking(int bookingId) {
        String findSql = "SELECT bus_id, seats_booked FROM bookings WHERE booking_id = ? FOR UPDATE";
        String deleteSql = "DELETE FROM bookings WHERE booking_id = ?";
        String restoreSql = "UPDATE buses SET available_seats = LEAST(total_seats, available_seats + ?) WHERE bus_id = ?";
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement find = connection.prepareStatement(findSql)) {
                    find.setInt(1, bookingId);
                    try (ResultSet row = find.executeQuery()) {
                        if (!row.next()) { connection.rollback(); return false; }
                        int busId = row.getInt("bus_id"), seats = row.getInt("seats_booked");
                        try (PreparedStatement delete = connection.prepareStatement(deleteSql); PreparedStatement restore = connection.prepareStatement(restoreSql)) {
                            delete.setInt(1, bookingId);
                            if (delete.executeUpdate() != 1) throw new SQLException("Booking was not deleted.");
                            restore.setInt(1, seats); restore.setInt(2, busId);
                            if (restore.executeUpdate() != 1) throw new SQLException("Seats were not restored.");
                        }
                    }
                }
                connection.commit();
                return true;
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            }
        } catch (SQLException exception) { System.err.println("Cancellation failed: " + exception.getMessage()); return false; }
    }

    private boolean isValidGender(String gender) { return "Male".equals(gender) || "Female".equals(gender) || "Other".equals(gender); }
}
