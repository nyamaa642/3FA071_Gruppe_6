package api.customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import api.database.Constants;
import api.database.DatabaseConnection;

import api.database.IDatabaseConnection;


public class CustomerDAO {

    Connection getConnection() throws SQLException {
        String url = Constants.databaseUrl;
        String username = Constants.username;
        String password = Constants.password;
        return DriverManager.getConnection(url, username, password);
    }


    // Create Customer
    public void addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO Customer (id, first_name, last_name, birth_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            UUID id = UUID.randomUUID();
            customer.setId(id);
            statement.setString(1, customer.getId().toString());
            statement.setString(2, customer.getFirstName());
            statement.setString(3, customer.getLastName());
            statement.setDate(4, Date.valueOf(customer.getBirthDate()));
            statement.executeUpdate();
        }
    }

    // Read all Customers
    public List<Customer> getAllCustomers() throws SQLException {
        String sql = "SELECT * FROM Customer";
        List<Customer> customers = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(UUID.fromString(resultSet.getString("id")));
                customer.setFirstName(resultSet.getString("first_name"));
                customer.setLastName(resultSet.getString("last_name"));
                customer.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                customers.add(customer);
            }
        }
        return customers;
    }

    // Read Customer by ID
    public Customer getCustomerById(UUID id) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE id = ?";
        Customer customer = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    customer = new Customer();
                    customer.setId(UUID.fromString(resultSet.getString("id")));
                    customer.setFirstName(resultSet.getString("first_name"));
                    customer.setLastName(resultSet.getString("last_name"));
                    customer.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                }
            }
        }
        return customer;
    }

    // Update Customer
    public boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE Customer SET first_name = ?, last_name = ?, birth_date = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setDate(3, Date.valueOf(customer.getBirthDate()));
            statement.setString(4, customer.getId().toString());
            statement.executeUpdate();
        }
        return false;
    }

    // Delete Customer
    public boolean deleteCustomer(UUID id) throws SQLException {
        String sql = "DELETE FROM Customer WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        }
        return false;
    }
}
