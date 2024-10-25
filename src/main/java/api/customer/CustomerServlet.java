package api.customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

@WebServlet("/customers")
public class CustomerServlet extends HttpServlet {
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        try {
            if (idParam == null) {
                response.setContentType("application/json");
                response.getWriter().write(customerDAO.getAllCustomers().toString());
            } else {
                UUID id = UUID.fromString(idParam);
                Customer customer = customerDAO.getCustomerById(id);
                if (customer != null) {
                    response.setContentType("application/json");
                    response.getWriter().write(customer.toString());
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
                }
            }
        } catch (SQLException e) {
            log("Database access error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String birthDateParam = request.getParameter("birthDate");

        if (firstName == null || lastName == null || birthDateParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            LocalDate birthDate = LocalDate.parse(birthDateParam);
            Customer newCustomer = new Customer(UUID.randomUUID(), firstName, lastName, birthDate);
            customerDAO.addCustomer(newCustomer);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("Customer created successfully");
        } catch (SQLException e) {
            log("Database access error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String birthDateParam = request.getParameter("birthDate");

        if (idParam == null || firstName == null || lastName == null || birthDateParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            UUID id = UUID.fromString(idParam);
            LocalDate birthDate = LocalDate.parse(birthDateParam);
            Customer updatedCustomer = new Customer(id, firstName, lastName, birthDate);
            if (customerDAO.updateCustomer(updatedCustomer)) {
                response.getWriter().write("Customer updated successfully");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
            }
        } catch (SQLException e) {
            log("Database access error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter: id");
            return;
        }

        try {
            UUID id = UUID.fromString(idParam);
            if (customerDAO.deleteCustomer(id)) {
                response.getWriter().write("Customer deleted successfully");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
            }
        } catch (SQLException e) {
            log("Database access error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }
}
