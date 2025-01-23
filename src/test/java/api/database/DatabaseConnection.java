package api.database;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseConnectionTest {

    private static DatabaseConnection dbInstance;
    private static Properties testProperties;
    private static final String TEST_DB_NAME = Constants.jUnitTestDb;

    @BeforeAll
    static void setUp() throws SQLException, ClassNotFoundException {

        testProperties = new Properties();
        testProperties.setProperty("db.url", "jdbc:mariadb://localhost:3306/");
        testProperties.setProperty("db.username", "root");
        testProperties.setProperty("db.password", "12345");

        dbInstance = DatabaseConnection.getInstance();
        dbInstance.openConnection(testProperties);


        Statement stmt = dbInstance.getConnection().createStatement();
        stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + TEST_DB_NAME);
        stmt.close();


        testProperties.setProperty("db.url", "jdbc:mariadb://localhost:3306/" + TEST_DB_NAME);
        dbInstance.openConnection(testProperties);
    }

    @Test
    @Order(1)
    void testSingletonInstance() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();

        assertSame(instance1, instance2, "Es sollte nur eine Instanz von DatabaseConnection geben.");
    }

    @Test
    @Order(2)
    void testOpenConnection() throws SQLException, ClassNotFoundException {
        dbInstance.openConnection(testProperties);
        Connection connection = dbInstance.getConnection();

        assertNotNull(connection, "Keine Verbindung zur DB - null connection.");
        assertFalse(connection.isClosed(), "Keine Verbindung zur DB - Die Verbindung ist closed.");
    }

    @Test
    @Order(3)
    void testCreateDatabaseAndTables() throws SQLException {
        dbInstance.createAllTables();

        Connection connection = dbInstance.getConnection();
        assertNotNull(connection, "Datenbankverbindung sollte aktiv sein.");

        // prüfung ob tabellen existieren
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW TABLES");

        assertTrue(rs.next(), "Es sollten Tabellen in der Datenbank existieren.");

        rs.close();
        stmt.close();
    }

    @Test
    @Order(4)
    void testCloseConnection() throws SQLException {
        dbInstance.closeConnection();
        Connection connection = dbInstance.getConnection();

        assertTrue(connection == null || connection.isClosed(), "Die Verbindung sollte geschlossen sein.");
    }

    @AfterAll
    static void tearDown() throws SQLException, ClassNotFoundException {
        dbInstance.openConnection(testProperties);
        Statement stmt = dbInstance.getConnection().createStatement();

//nach test db löschen
        stmt.executeUpdate("DROP DATABASE " + TEST_DB_NAME);
        stmt.close();

        dbInstance.closeConnection();
    }
}
