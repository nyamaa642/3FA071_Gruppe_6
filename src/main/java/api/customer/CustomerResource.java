package api.customer;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/customers")
public class CustomerResource {

    private static CustomerDAO customerDAO;

    @GET
    @Path("/{id : [0-9a-fA-F\\-]{36}}")

    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerById(@PathParam("id") UUID id) throws SQLException {
        Customer customer = customerDAO.getCustomerById(id);
        return Response.status(Response.Status.OK)
                .entity(customer)
                .build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public  Response getAllCustomer() throws SQLException {
        List<Customer> customers = customerDAO.getAllCustomers();
        return Response.status(Response.Status.OK)
                .entity(customers)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomer() throws SQLException {
        Customer customer = new Customer();
        customerDAO.addCustomer(customer);
        return Response.status(Response.Status.CREATED)
                .entity(customer)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@PathParam("id") UUID id, Customer updatedCustomer) throws SQLException {
        Customer existingCustomer = customerDAO.getCustomerById(id);
        if (existingCustomer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existingCustomer.setLastName(updatedCustomer.getFirstName());
        existingCustomer.setFirstName(updatedCustomer.getLastName());
        existingCustomer.setBirthDate(updatedCustomer.getBirthDate());
        customerDAO.updateCustomer(existingCustomer);
        return Response.status(Response.Status.OK)
                .entity(updatedCustomer)
                .build();

    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") UUID id) throws SQLException {
        Customer existingCustomer = customerDAO.getCustomerById(id);
        if (existingCustomer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        customerDAO.deleteCustomer(id);
        return Response.noContent().build();
    }
}
