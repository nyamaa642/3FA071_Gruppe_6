import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;
import java.util.stream.Collectors;

public class Main {
    //DatabaseHander dbHandler = new DatabaseHander(); //TODO Datenbankthemen in die Klasse auslagern.
                                                    // nur wegen Tests in main

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        new Main().doit();
    }



//doit um aus statischem rauszukommen
    void doit() throws SQLException, ClassNotFoundException {

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
