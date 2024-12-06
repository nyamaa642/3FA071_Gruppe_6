package api.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerDAOTest {

    private CustomerDAO customerDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        customerDAO = new CustomerDAO();
    }

    @Test
    public void testAddCustomer() throws SQLException {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "John", "Doe", LocalDate.of(1980, 1, 1));
        customerDAO.addCustomer(customer);

        Customer retrievedCustomer = customerDAO.getCustomerById(id);

        assertEquals(customer.getId(), retrievedCustomer.getId());
        assertEquals(customer.getFirstName(), retrievedCustomer.getFirstName());
        assertEquals(customer.getLastName(), retrievedCustomer.getLastName());
        assertEquals(customer.getBirthDate(), retrievedCustomer.getBirthDate());
    }

    @Test
    public void testGetAllCustomers() throws SQLException {
        Customer customer1 = new Customer(UUID.randomUUID(), "John", "Doe", LocalDate.of(1980, 1, 1));
        Customer customer2 = new Customer(UUID.randomUUID(), "Jane", "Smith", LocalDate.of(1990, 2, 2));
        Customer customer3 = new Customer(UUID.randomUUID(), "Mike", "Johnson", LocalDate.of(1970, 3, 3));

        customerDAO.addCustomer(customer1);
        customerDAO.addCustomer(customer2);
        customerDAO.addCustomer(customer3);
        List<Customer> retrievedCustomers = customerDAO.getAllCustomers();

        assertEquals(3, retrievedCustomers.size());
        assertTrue(retrievedCustomers.contains(customer1));
        assertTrue(retrievedCustomers.contains(customer2));
        assertTrue(retrievedCustomers.contains(customer3));
    }

    @Test
    public void testUpdateCustomer() throws SQLException {
        Customer customer = new Customer(UUID.randomUUID(), "John", "Doe", LocalDate.of(1980, 1, 1));
        customerDAO.addCustomer(customer);
        customer.setFirstName("Jane");
        customerDAO.updateCustomer(customer);
        Customer updatedCustomer = customerDAO.getCustomerById(customer.getId());
        assertEquals("Jane", updatedCustomer.getFirstName());
    }

    @Test
    public void testDeleteCustomer() throws SQLException {
        Customer customer = new Customer(UUID.randomUUID(), "John", "Doe", LocalDate.of(1980, 1, 1));
        customerDAO.addCustomer(customer);
        customerDAO.deleteCustomer(customer.getId());
        Customer retrievedCustomer = customerDAO.getCustomerById(customer.getId());
        assertNull(retrievedCustomer);
    }

}