import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) throws SQLException {
        new Main().doit();
    }



//doit um aus statischem rauszukommen
    void doit() throws SQLException {
    getDatabaseConn();


    }


    void getDatabaseConn() throws SQLException {
        Connection connection = DriverManager.
                getConnection("jdbc:mariadb://127.0.0.1:3306/DB?user="
                        + Constants.username + "&password=" + Constants.password);

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(String.valueOf(getClass().getClassLoader().getResource("C:\\Users\\it_hoppenz\\IdeaProjects\\3FA071_Gruppe_6\\src\\main" +
                "\\resources\\databasemigrations\\V1__customer-schema.sql")));
        stmt.close();
        connection.close();
    }
}
