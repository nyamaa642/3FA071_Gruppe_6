package api.customer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.UUID;

import static org.junit.Assert.*;

public class CustomerDAOTest {

    private CustomerDAO customerDAO;

    @Before
    public void setUp() throws SQLException {
        customerDAO = new CustomerDAO();

        // Set up the test database schema
        try (Connection connection = customerDAO.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS customers (" +
                    "id CHAR(36) PRIMARY KEY, " +
                    "firstname VARCHAR(50), " +
                    "lastname VARCHAR(50), " +
                    "birthDate DATE)");
        }
    }

    @After
    public void tearDown() throws SQLException {
        // Clean up the test database after each test
        try (Connection connection = customerDAO.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE customers");
        }
    }

    @Test
    public void testAddCustomer() throws SQLException {
        Customer customer = new Customer(UUID.randomUUID(), "John", "Doe", java.time.LocalDate.of(1990, 1, 1));
        customerDAO.addCustomer(customer);

        // Verify that the customer was added
        Customer retrievedCustomer = customerDAO.getCustomerById(customer.getId());
        assertNotNull(retrievedCustomer);
        assertEquals("John", retrievedCustomer.getFirstName());
        assertEquals("Doe", retrievedCustomer.getLastName());
        assertEquals(java.time.LocalDate.of(1990, 1, 1), retrievedCustomer.getBirthDate());
    }

    @Test
    public void testGetCustomerById() throws SQLException {
        // Insert a customer directly to the DB for testing
        UUID id = UUID.randomUUID();
        try (Connection connection = customerDAO.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO customers (id, firstname, lastname, birthDate) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, id.toString());
            statement.setString(2, "Jane");
            statement.setString(3, "Doe");
            statement.setDate(4, Date.valueOf("1992-02-02"));
            statement.executeUpdate();
        }

        // Retrieve the customer and check values
        Customer customer = customerDAO.getCustomerById(id);
        assertNotNull(customer);
        assertEquals("Jane", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals(java.time.LocalDate.of(1992, 2, 2), customer.getBirthDate());
    }

}
