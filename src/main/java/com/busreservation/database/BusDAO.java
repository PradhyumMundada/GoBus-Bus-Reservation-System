package com.busreservation.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/** Data-access methods for buses. PreparedStatement keeps user input out of SQL text. */
public class BusDAO {
    public ArrayList<Bus> getAllBuses() {
        return readBuses("SELECT * FROM buses ORDER BY bus_id", null, null);
    }

    public ArrayList<Bus> searchBuses(String source, String destination) {
        return readBuses("SELECT * FROM buses WHERE source = ? AND destination = ? ORDER BY bus_id", source, destination);
    }

    private ArrayList<Bus> readBuses(String sql, String source, String destination) {
        ArrayList<Bus> buses = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (source != null) {
                statement.setString(1, source.trim());
                statement.setString(2, destination.trim());
            }
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    buses.add(new Bus(result.getInt("bus_id"), result.getString("bus_number"),
                            result.getString("bus_name"), result.getString("source"), result.getString("destination"),
                            result.getTime("departure_time").toString(), result.getTime("arrival_time").toString(),
                            result.getInt("total_seats"), result.getInt("available_seats"), result.getDouble("fare")));
                }
            }
        } catch (SQLException exception) {
            System.err.println("Could not read buses: " + exception.getMessage());
        }
        return buses;
    }
}
