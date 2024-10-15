import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        new Main().doit();
    }



//doit um aus statischem rauszukommen
    void doit(){



    }


    void getDatabaseConn() throws SQLException {
        Connection connection = DriverManager.
                getConnection("jdbc:mariadb://localhost:3306/DB?user="
                        + Constants.username + "&password=" + Constants.password);

        Statement stmt = connection.createStatement();
        stmt.executeUpdate("SELECT * FROM...");
        stmt.close();
        connection.close();
    }
}
