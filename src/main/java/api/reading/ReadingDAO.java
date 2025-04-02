package api.reading;

import api.database.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReadingDAO {

    Connection getConnection() throws SQLException {
        String url = Constants.databaseUrl;
        String username = Constants.username;
        String password = Constants.password;
        return DriverManager.getConnection(url, username, password);
    }

    // Create Reading
    public void addReading(Reading reading) throws SQLException {
        String sql = "INSERT INTO Reading (id, customer_id, type_of_meter, reading_count) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            UUID id = UUID.randomUUID();
            reading.setId(id);
            statement.setString(1, reading.getId().toString());
            statement.setString(2, reading.getCustomerID().toString());
            statement.setString(3, reading.getType().toString());
            statement.setDouble(4, reading.getReading());
            statement.executeUpdate();
        }
    }

    // List all Reading
    public List<Reading> getAllReadings() throws SQLException {
        List<Reading> readings = new ArrayList<>();
        String sql = "SELECT * FROM Reading";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Reading reading = new Reading();
                reading.setId(UUID.fromString(resultSet.getString("id")));
                reading.setId(UUID.fromString(resultSet.getString("customer_id")));
                reading.setType(TypeOfMeter.valueOf(resultSet.getString("type_of_meter")));
                reading.setReading(resultSet.getDouble("reading_count"));
                readings.add(reading);
            }
        }
        return readings;
    }

    // Get Reading by ID
    public Reading getReadingById(UUID id) throws SQLException {
        Reading reading = new Reading();
        String sql = "SELECT * FROM Reading WHERE id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                reading.setId(UUID.fromString(resultSet.getString("id")));
                reading.setId(UUID.fromString(resultSet.getString("customer_id")));
                reading.setType(TypeOfMeter.valueOf(resultSet.getString("type_of_meter")));
                reading.setReading(resultSet.getDouble("reading_count"));
            }
        }
        return reading;
    }

    // Update Reading
    public boolean updateReading(Reading reading) throws SQLException {
        String sql = "UPDATE Reading SET customer_id = ?, type_of_meter = ?, reading_count = ? WHERE id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reading.getCustomerID().toString());
            statement.setString(2, reading.getType().toString());
            statement.setDouble(3, reading.getReading());
            statement.setString(5, reading.getId().toString());
            statement.executeUpdate();
        }
        return false;
    }

    // Delete Reading
    public void deleteReading(UUID id) throws SQLException {
        String sql = "DELETE FROM Reading WHERE id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            statement.executeUpdate();

        }
    }
}
