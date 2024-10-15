import java.sql.*;
import java.util.List;


public abstract class DataAccessObject<T> {

    // Abstract methods to be implemented by concrete DAOs
    public abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    public abstract String getTableName();

    protected Connection getConnection() throws SQLException {
        // Assume DriverManager.getConnection() is properly set up here
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "root", "your_password");
    }

    public T findById(int id) throws SQLException {
        String query = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToEntity(resultSet);
            }
        }
        return null;
    }

    public void deleteById(int id) throws SQLException {
        String query = "DELETE FROM " + getTableName() + " WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public abstract void insert(T entity) throws SQLException;

    public abstract List<T> findAll() throws SQLException;
}
