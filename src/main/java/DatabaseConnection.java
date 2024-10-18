import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;
import java.util.stream.Collectors;

public class DatabaseConnection implements IDatabaseConnection{


    private Connection connection;

    @Override
    public IDatabaseConnection openConnection(Properties properties) throws SQLException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        this.connection = DriverManager.getConnection(url, username, password);
        return this;

    }


    @Override
    public void createAllTables() throws SQLException {
        Statement stmt = connection.createStatement();

        //Datenbank neu aufsetzen:
        String dbName = Constants.databaseName;

        // prüfen ob die db schon exisitert
        if (databaseExists(dbName)) {
            System.out.println("Datenbank exisitert bereits, wird neu aufgesetzt...");
            stmt.executeUpdate("DROP DATABASE " + dbName);
        }
        //stmt.executeUpdate("DROP DATABASE " + Constants.databaseName);
        System.out.println("Datenbank " + Constants.databaseName + " erstellen...");
        stmt.executeUpdate("CREATE DATABASE " + Constants.databaseName);
        stmt.executeUpdate("USE test");



        String sqlV1 = readSQLFromFile("/databasemigrations/V1__kunden-schema.txt");
        String sqlV2 = readSQLFromFile("/databasemigrations/V2__reading-schema.txt");

        System.out.println("Tabellen erstellen...");//\n " + sqlV1 + " & \n" +sqlV2);

        stmt.executeUpdate(sqlV1);
        stmt.executeUpdate(sqlV2);
    }

    @Override
    public void truncateAllTables() throws SQLException {

    }

    @Override
    public void removeAllTables() throws SQLException {

    }

    @Override
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private String readSQLFromFile(String resourcePath) {
        InputStream inputStream = getClass().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new RuntimeException("Datei nicht gefunden, Pfad überprüfen!: " + resourcePath);
        }
        // Datei in einen String umwandeln
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private boolean databaseExists(String dbName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW DATABASES LIKE '" + dbName + "'");
        boolean exists = rs.next();
        rs.close();
        stmt.close();
        return exists;
    }
}
