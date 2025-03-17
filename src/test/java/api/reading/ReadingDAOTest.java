package api.reading;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public class ReadingDAOTest {

    private ReadingDAO readingDAO;
    private UUID testCustomerId;

    @BeforeEach
    void setUp() throws SQLException {
        readingDAO = new ReadingDAO();
        testCustomerId = UUID.randomUUID();

        // Datenbank für Tests zurücksetzen
        try (Connection connection = readingDAO.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM readings");
        }
    }

    @Test
    void testAddAndGetReading() throws SQLException {
        // Testdaten erstellen
        Reading reading = new Reading();
        reading.setCustomerID(testCustomerId);
        reading.setType(TypeOfMeter.ELECTRICITY);
        reading.setReading(150.75);

        // Reading hinzufügen
        readingDAO.addReading(reading);

        // Reading abrufen
        Reading retrieved = readingDAO.getReadingById(reading.getId());

        // Überprüfungen
        assertNotNull(retrieved.getId());
        assertEquals(testCustomerId, retrieved.getCustomerID());
        assertEquals(TypeOfMeter.ELECTRICITY, retrieved.getType());
        assertEquals(150.75, retrieved.getReading(), 0.001);
    }

    @Test
    void testGetAllReadings() throws SQLException {
        // Zwei Test-Readings erstellen
        Reading r1 = createTestReading(100.0);
        Reading r2 = createTestReading(200.0);

        readingDAO.addReading(r1);
        readingDAO.addReading(r2);

        // Alle Readings abrufen
        List<Reading> readings = readingDAO.getAllReadings();

        // Überprüfungen
        assertEquals(2, readings.size());
        assertTrue(readings.stream().anyMatch(r -> r.getReading() == 100.0));
        assertTrue(readings.stream().anyMatch(r -> r.getReading() == 200.0));
    }

    @Test
    void testUpdateReading() throws SQLException {
        // Test-Reading erstellen
        Reading reading = createTestReading(100.0);
        readingDAO.addReading(reading);

        // Reading aktualisieren
        reading.setReading(200.0);
        reading.setType(TypeOfMeter.GAS);
        readingDAO.updateReading(reading);

        // Aktualisiertes Reading abrufen
        Reading updated = readingDAO.getReadingById(reading.getId());

        // Überprüfungen
        assertEquals(200.0, updated.getReading(), 0.001);
        assertEquals(TypeOfMeter.GAS, updated.getType());
    }

    @Test
    void testDeleteReading() throws SQLException {
        // Test-Reading erstellen
        Reading reading = createTestReading(150.0);
        readingDAO.addReading(reading);

        // Reading löschen
        readingDAO.deleteReading(reading.getId());

        // Gelöschtes Reading abrufen
        Reading deleted = readingDAO.getReadingById(reading.getId());

        // Überprüfung
        assertNull(deleted.getId());
    }

    @Test
    void testGetNonExistentReading() throws SQLException {
        Reading result = readingDAO.getReadingById(UUID.randomUUID());
        assertNull(result.getId());
    }

    private Reading createTestReading(double value) {
        Reading reading = new Reading();
        reading.setCustomerID(testCustomerId);
        reading.setType(TypeOfMeter.WATER);
        reading.setReading(value);
        return reading;
    }
}