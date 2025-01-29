package api.customer;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/customers")
public class CustomerResource {

    private static List<Customer> customers = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> getCustomers(){
        return customers;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Customer createCustomer(Customer customer) {
        customers.add(customer);
        return customer;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Customer updateCustomer(@PathParam("id") UUID id, Customer customer) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId() == id) {
                customers.set(i, customer);
                return customer;
            }
        }
        throw new NotFoundException("Customer with ID " + id + " not found.");
    }

    @DELETE
    @Path("/{id}")
    public void deleteCustomer(@PathParam("id") UUID id) {
        customers.removeIf(customer -> customer.getId() == id);
    }
}
