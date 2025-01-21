package api.reading;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReadingDAO {

    Connection getConnection() throws SQLException {
        String url = "jdbc:mariadb://127.0.0.1:3306/";
        String username = "root";
        String password = "12345";
        return DriverManager.getConnection(url, username, password);
    }

    // Create Reading
    public void addReading(Reading reading) throws SQLException {
        String sql = "INSERT INTO readings (id, reading_id, typeOfMeter, reading) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            reading.setId(UUID.randomUUID());
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
        String sql = "SELECT * FROM house_readings";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Reading reading = new Reading();
                reading.setId(UUID.fromString(resultSet.getString("id")));
                reading.setId(UUID.fromString(resultSet.getString("customer_id")));
                reading.setType(TypeOfMeter.valueOf(resultSet.getString("typeOfMeter")));
                reading.setReading(resultSet.getDouble("reading"));
                readings.add(reading);
            }
        }
        return readings;
    }

    // Get Reading by ID
    public Reading getReadingById(UUID id) throws SQLException {
        Reading reading = new Reading();
        String sql = "SELECT * FROM readings WHERE id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                reading.setId(UUID.fromString(resultSet.getString("id")));
                reading.setId(UUID.fromString(resultSet.getString("customer_id")));
                reading.setType(TypeOfMeter.valueOf(resultSet.getString("typeOfMeter")));
                reading.setReading(resultSet.getDouble("reading"));
            }
        }
        return reading;
    }

    // Update Reading
    public boolean updateReading(Reading reading) throws SQLException {
        String sql = "UPDATE readings SET customer_id = ?, meter_type = ?, reading_value = ?, reading_timestamp = ? WHERE id = ?";
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
        String sql = "DELETE FROM readings WHERE id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            statement.executeUpdate();

        }
    }
}
