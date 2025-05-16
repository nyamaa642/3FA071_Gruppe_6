package api.database;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseGUI extends JFrame {
    private final DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    private final JTextArea logArea = new JTextArea();
    private final JTextField urlField = new JTextField();
    private final JTextField userField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public DatabaseGUI() {
        initializeUI();
        loadDefaultConfig();
    }

    private void initializeUI() {
        setTitle("Database Management");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Connection Panel
        JPanel connectionPanel = createConnectionPanel();

        // Control Panel
        JPanel controlPanel = createControlPanel();

        // Log Panel
        JScrollPane logScroll = new JScrollPane(logArea);
        logArea.setEditable(false);

        add(connectionPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        add(logScroll, BorderLayout.SOUTH);
    }

    private JPanel createConnectionPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Verbindungseinstellungen"));

        JButton loadConfigBtn = new JButton("Lade Konfiguration");
        loadConfigBtn.addActionListener(e -> loadDefaultConfig());

        JButton connectBtn = new JButton("Verbinden");
        connectBtn.addActionListener(e -> connect());

        panel.add(new JLabel("DB URL:"));
        panel.add(urlField);
        panel.add(new JLabel("Benutzer:"));
        panel.add(userField);
        panel.add(new JLabel("Passwort:"));
        panel.add(passwordField);
        panel.add(loadConfigBtn);
        panel.add(connectBtn);

        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Datenbank Operationen"));

        JButton createTablesBtn = createOperationButton("Tabellen erstellen", this::createTables);
        JButton truncateBtn = createOperationButton("Tabellen leeren", this::truncateTables);
        JButton removeTablesBtn = createOperationButton("Tabellen löschen", this::removeTables);
        JButton closeBtn = createOperationButton("Verbindung schließen", this::closeConnection);

        panel.add(createTablesBtn);
        panel.add(truncateBtn);
        panel.add(removeTablesBtn);
        panel.add(closeBtn);

        return panel;
    }

    private JButton createOperationButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> {
            try {
                action.run();
            } catch (Exception ex) {
                logError(ex.getMessage());
            }
        });
        return button;
    }

    private void loadDefaultConfig() {
        try {
            urlField.setText(DatabaseConfig.getDbUrl());
            userField.setText(DatabaseConfig.getDbUser());
            passwordField.setText(DatabaseConfig.getDbPassword());
            log("Standardkonfiguration geladen");
        } catch (Exception e) {
            logError("Konfiguration konnte nicht geladen werden: " + e.getMessage());
        }
    }

    private void connect() {
        try {
            Properties props = new Properties();
            props.setProperty("db.url", urlField.getText());
            props.setProperty("db.username", userField.getText());
            props.setProperty("db.password", new String(passwordField.getPassword()));

            dbConnection.openConnection(props);
            log("Erfolgreich verbunden mit: " + urlField.getText());
        } catch (Exception e) {
            logError("Verbindungsfehler: " + e.getMessage());
        }
    }

    private void createTables() {
        try {
            dbConnection.createAllTables();
            log("Tabellen erfolgreich erstellt");
        } catch (Exception e) {
            logError("Tabellenerstellung fehlgeschlagen: " + e.getMessage());
        }
    }

    private void truncateTables() {
        try {
            dbConnection.truncateAllTables();
            log("Tabellen erfolgreich geleert");
        } catch (Exception e) {
            logError("Fehler beim Leeren der Tabellen: " + e.getMessage());
        }
    }

    private void removeTables() {
        try {
            dbConnection.removeAllTables();
            log("Tabellen erfolgreich gelöscht");
        } catch (Exception e) {
            logError("Fehler beim Löschen der Tabellen: " + e.getMessage());
        }
    }

    private void closeConnection() {
        try {
            dbConnection.closeConnection();
            log("Verbindung erfolgreich geschlossen");
        } catch (SQLException e) {
            logError("Fehler beim Schließen der Verbindung: " + e.getMessage());
        }
    }

    private void log(String message) {
        logArea.append("[OK] " + message + "\n");
    }

    private void logError(String message) {
        logArea.append("[ERROR] " + message + "\n");
    }
}