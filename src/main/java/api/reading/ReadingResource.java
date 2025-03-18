package api.reading;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Path("/readings")
public class ReadingResource {
    
    ReadingDAO readingDAO = new ReadingDAO();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadingById(@PathParam("id") UUID id) throws SQLException {
        Reading reading = readingDAO.getReadingById(id);
        return Response.status(Response.Status.OK)
                .entity(reading)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReading() throws SQLException {
        List<Reading> readings = readingDAO.getAllReadings();
        return Response.status(Response.Status.OK)
                .entity(readings)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReading() throws SQLException {
        Reading reading = new Reading();
        readingDAO.addReading(reading);
        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReading(@PathParam("id") UUID id, Reading updatedReading) throws SQLException {
        Reading existingReading = readingDAO.getReadingById(id);
        if (existingReading == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existingReading.setCustomerID(updatedReading.getCustomerID());
        existingReading.setType(updatedReading.getType());
        existingReading.setReading(updatedReading.getReading());
        readingDAO.updateReading(existingReading);
        return Response.status(Response.Status.OK)
                .entity(updatedReading)
                .build();

    }

    @DELETE
    @Path("/{id}")
    public Response deleteReading(@PathParam("id") UUID id) throws SQLException {
        Reading existingReading = readingDAO.getReadingById(id);
        if (existingReading == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        readingDAO.deleteReading(id);
        return Response.noContent().build();
    }
}
