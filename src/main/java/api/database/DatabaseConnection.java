package api.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;
import java.util.stream.Collectors;

public class DatabaseConnection implements IDatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    public DatabaseConnection() {}

    //nutze nur eine instanz
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    @Override
    public IDatabaseConnection openConnection(Properties properties) throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) { // db verbindung aufbauen, wenn keine offen ist
            Class.forName("org.mariadb.jdbc.Driver");
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");

            this.connection = DriverManager.getConnection(url, username, password);
        }
        return this;
    }

    @Override
    public void createAllTables() throws SQLException {
        Statement stmt = connection.createStatement();

        // db name geben
        String dbName = Constants.databaseName;


        // prüfe, ob die db bereits existiert
        if (databaseExists(dbName)) {
            System.out.println("Produktiv Datenbank existiert bereits, wird neu aufgesetzt...");
            stmt.executeUpdate("DROP DATABASE " + dbName);
        }

        System.out.println("Datenbank " + Constants.databaseName + " wird erstellt...");
        stmt.executeUpdate("CREATE DATABASE " + Constants.databaseName);
        stmt.executeUpdate("USE " + Constants.databaseName);

        String sqlV1 = readSQLFromFile("/databasemigrations/V1__kunden-schema.sql");
        String sqlV2 = readSQLFromFile("/databasemigrations/V2__reading-schema.sql");

        System.out.println("Tabellen werden erstellt...");
        stmt.executeUpdate(sqlV1);
        stmt.executeUpdate(sqlV2);
    }

    @Override
    public void truncateAllTables() throws SQLException {
        // TODO: Implementieren
    }

    @Override
    public void removeAllTables() throws SQLException {
        // TODO: Implementieren
    }

    @Override
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null; // Verbindung auf null setzen ->damit sie neu aufgebaut werden kann
        }
    }

    private String readSQLFromFile(String resourcePath) {
        InputStream inputStream = getClass().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new RuntimeException("Datei nicht gefunden, Pfad überprüfen!: " + resourcePath);
        }
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    boolean databaseExists(String dbName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW DATABASES LIKE '" + dbName + "'");
        boolean exists = rs.next();
        rs.close();
        stmt.close();
        return exists;
    }

    // Getter für die Connection (für Tests notwendig)
    public Connection getConnection() {
        return connection;
    }
}
