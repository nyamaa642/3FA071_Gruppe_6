import java.sql.*;

public class Main {
    DatabaseHander dbHandler = new DatabaseHander(); //TODO Datenbankthemen in die Klasse auslagern.
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
        stmt.executeUpdate("use test");
        String selectSQL = "SELECT * FROM testtable";
        ResultSet rs = stmt.executeQuery(selectSQL);
        while(rs.next()){
            String value = rs.getString("spalte1");

            System.out.println("spalte1: " + value);
        }
       // stmt.executeUpdate(String.valueOf(getClass().getClassLoader().getResource("C:\\Users\\it_hoppenz\\IdeaProjects\\3FA071_Gruppe_6\\src\\main" +
        //        "\\resources\\databasemigrations\\V1__customer-schema.sql")));
        stmt.close();
        connection.close();
    }
}
