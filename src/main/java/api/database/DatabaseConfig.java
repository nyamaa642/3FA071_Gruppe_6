package api.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DatabaseConfig {
    private static final Map<String, String> dbSettings = new HashMap<>();

    static {
        String fileName = "PropertiesDBConnection.properties";

        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

            if (input == null) {
                throw new RuntimeException("Datei '" + fileName + "' nicht gefunden! Prüfe den Pfad in src/main/resources.");
            }

            Properties properties = new Properties();
            properties.load(reader);

            // system username holen
            String systemUser = System.getProperty("user.name");
            System.out.println("Erkannter Systemnutzer: " + systemUser);

            // werte für den o.g nutzer holen
            String userConfig = properties.getProperty(systemUser);

            if (userConfig == null) {
                throw new RuntimeException("Kein DB Setup für User: " + systemUser + " in Properties-Datei gefunden!");
            }
            //werte aus der properties zeile parsen

            String[] keyValuePairs = userConfig.split(";");
            for (String pair : keyValuePairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    dbSettings.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("fehler beim Laden der Konfigurationsdatei", e);
        }
    }

    public static String getDbUrl() {
        return dbSettings.get("dbUrl");
    }

    public static String getDbUser() {
        return dbSettings.get("dbUser");
    }

    public static String getDbPassword() {
        return dbSettings.get("dbPw");
    }
}
