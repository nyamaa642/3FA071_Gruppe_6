package api.customer;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.sql.DriverManager.getConnection;

public class CustomerDAO {

    // Create Customer
    public void addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (id, firstname, lastname, birthDate) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            customer.setId(UUID.randomUUID());
            statement.setString(1, customer.getId().toString());
            statement.setString(2, customer.getFirstName());
            statement.setString(3, customer.getLastName());
            statement.setDate(4, Date.valueOf(customer.getBirthDate()));
            statement.executeUpdate();
        }
    }

    // Read all Customers
    public List<Customer> getAllCustomers() throws SQLException {
        String sql = "SELECT * FROM customers";
        List<Customer> customers = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(UUID.fromString(resultSet.getString("id")));
                customer.setFirstName(resultSet.getString("firstname"));
                customer.setLastName(resultSet.getString("lastname"));
                customer.setBirthDate(resultSet.getDate("birthDate").toLocalDate());
                customers.add(customer);
            }
        }
        return customers;
    }

    // Read Customer by ID
    public Customer getCustomerById(UUID id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE id = ?";
        Customer customer = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    customer = new Customer();
                    customer.setId(UUID.fromString(resultSet.getString("id")));
                    customer.setFirstName(resultSet.getString("firstname"));
                    customer.setLastName(resultSet.getString("lastname"));
                    customer.setBirthDate(resultSet.getDate("birthDate").toLocalDate());
                }
            }
        }
        return customer;
    }

    // Update Customer
    public void updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET firstname = ?, lastname = ?, birthDate = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setDate(3, Date.valueOf(customer.getBirthDate()));
            statement.setString(4, customer.getId().toString());
            statement.executeUpdate();
        }
    }

    // Delete Customer
    public void deleteCustomer(UUID id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        }
    }
}
