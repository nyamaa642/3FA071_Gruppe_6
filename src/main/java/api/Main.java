package api;


import api.customer.CustomerGUI;
import api.database.Constants;
import api.database.DatabaseConnection;
import api.database.DatabaseGUI;
import api.database.IDatabaseConnection;
import api.reading.ReadingGUI;

import javax.swing.SwingUtilities;
import java.sql.SQLException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // Server starten
        ApiServer server = new ApiServer();
        server.startServer();

        // GUI starten
        //startDatabaseGUI();
        // Oder ReadingGUI starten:
         startReadingGUI();

        // Datenbankverbindung testen
        databaseConnectionTest();
    }

    private static void startDatabaseGUI() {
        SwingUtilities.invokeLater(() -> {
            DatabaseGUI gui = new DatabaseGUI();
            gui.setLocationRelativeTo(null);
            gui.setVisible(true);
        });
    }

    private static void startReadingGUI() {
        SwingUtilities.invokeLater(() -> {
            ReadingGUI gui = new ReadingGUI();
            gui.setLocationRelativeTo(null);
            gui.setVisible(true);
        });
    }

    private static void startCustomerGUI()  {
        SwingUtilities.invokeLater(() -> {
            CustomerGUI gui = new CustomerGUI();
            gui.setLocationRelativeTo(null);
            gui.setVisible(true);
            });
        }


    private static void databaseConnectionTest() throws SQLException, ClassNotFoundException {
        Properties properties = new Properties();
        properties.setProperty("db.url", Constants.databaseUrl);
        properties.setProperty("db.username", Constants.username);
        properties.setProperty("db.password", Constants.password);

        IDatabaseConnection dbConn = null;
        try {
            dbConn = new DatabaseConnection().openConnection(properties);
            dbConn.createAllTables();
        } finally {
            if (dbConn != null) {
                dbConn.closeConnection();
            }
        }
    }
}