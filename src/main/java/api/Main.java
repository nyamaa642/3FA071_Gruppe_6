package api;

import api.database.Constants;
import api.database.DatabaseConnection;
import api.database.IDatabaseConnection;

import java.sql.SQLException;
import java.util.Properties;
//import java.util.Properties;

public class Main {
    //api.database.DatabaseHander dbHandler = new api.database.DatabaseHander(); //TODO Datenbankthemen in die Klasse auslagern.
                                                    // nur wegen Tests in main

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        ApiServer server = new ApiServer();
        server.startServer();
        new Main().doit();
    }



//doit um aus statischem rauszukommen
    void doit() throws SQLException, ClassNotFoundException {
        //DatabaseConfig dbc = new DatabaseConfig();


        databaseConnectionTestInterface();


    }

    static void databaseConnectionTestInterface() throws SQLException, ClassNotFoundException {

        Properties properties = new Properties();
        properties.setProperty("db.url", Constants.databaseUrl);
        properties.setProperty("db.username", Constants.username);
        properties.setProperty("db.password", Constants.password);

        IDatabaseConnection dbConn = new DatabaseConnection().openConnection(properties);
        dbConn.createAllTables();


        dbConn.closeConnection();
    }

}
