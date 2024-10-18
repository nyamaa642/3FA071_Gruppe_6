import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.stream.Collectors;

public class Main {
    //DatabaseHander dbHandler = new DatabaseHander(); //TODO Datenbankthemen in die Klasse auslagern.
                                                    // nur wegen Tests in main

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        new Main().doit();
    }



//doit um aus statischem rauszukommen
    void doit() throws SQLException, ClassNotFoundException {
       // dbHandler.
//        System.out.println("START, printing string of sql");
//        System.out.println(String.valueOf(getClass().getClassLoader().getResource("C:\\Users\\it_hoppenz\\IdeaProjects\\3FA071_Gruppe_6\\src\\main" +
//                "\\resources\\databasemigrations\\V1__customer-schema.txt")));
       // System.out.println
        //das ist der pfad zu den sql create dateien
                //(getClass().getClassLoader().getResource("\\databasemigrations\\V1_kunden-schema.txt"));
        //
        getDatabaseConn();


    }


    void getDatabaseConn() throws SQLException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        Connection connection = DriverManager.
                getConnection("jdbc:mariadb://127.0.0.1:3306/", Constants.username, Constants.password);
                      //  + Constants.username + "&password=" + Constants.password);

        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP DATABASE TEST");
        stmt.executeUpdate("CREATE DATABASE TEST");
        stmt.executeUpdate("use test");
        // SQL aus der Datei lesen
        String sqlV1 = readSQLFromFile("/databasemigrations/V1__kunden-schema.txt");
        String sqlV2 = readSQLFromFile("/databasemigrations/V2__reading-schema.txt");
        // SQL ausführen
        stmt.executeUpdate(sqlV1);
        stmt.executeUpdate(sqlV2);

        stmt.close();
        connection.close();
    }


    // Methode zum lesen der sql/txt datei
    private String readSQLFromFile(String resourcePath) {
        InputStream inputStream = getClass().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new RuntimeException("Datei nicht gefunden, evtl. Falsch geschrieben??: " + resourcePath);
        }
        // Datei in einen String umwandeln
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
