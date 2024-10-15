import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        new Main().doit();
    }




    void doit(){



    }


    void getDatabaseConn() throws SQLException {
        Connection connection = DriverManager.
                getConnection("jdbc:mariadb://localhost:3306/DB?user=" + Constants.username + "&password=" + Constants.password);
    }
}
