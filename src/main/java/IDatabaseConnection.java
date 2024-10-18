import java.sql.SQLException;
import java.util.Properties;

public interface IDatabaseConnection {
    IDatabaseConnection openConnection(Properties properties) throws SQLException, ClassNotFoundException;
    void createAllTables() throws SQLException;
    void truncateAllTables() throws SQLException;
    void removeAllTables() throws SQLException;
    void closeConnection() throws SQLException;
}
