package api.reading;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.hausfix.utils.DatabaseConnection.getConnection;

public class ReadingDAO {

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
                reading.setCustomerID(UUID.fromString(resultSet.getString("customer_id")));
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
                reading.setCustomerID(UUID.fromString(resultSet.getString("customer_id")));
                reading.setType(TypeOfMeter.valueOf(resultSet.getString("typeOfMeter")));
                reading.setReading(resultSet.getDouble("reading"));
            }
        }
        return reading;
    }

    // Update Reading
    public void updateReading(Reading reading) throws SQLException {
        String sql = "UPDATE readings SET customer_id = ?, meter_type = ?, reading_value = ?, reading_timestamp = ? WHERE id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reading.getCustomerID().toString());
            statement.setString(2, reading.getType().toString());
            statement.setDouble(3, reading.getReading());
            statement.setString(5, reading.getId().toString());
            statement.executeUpdate();
        }
    }

    // Delete Reading
    public void deleteReading(UUID id) throws SQLException {
        String sql = "DELETE FROM house_readings WHERE id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            statement.executeUpdate();

        }
    }
}
