package api.reading;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet("/readings")
public class ReadingServlet extends HttpServlet {

    private final ReadingDAO readingDAO;

    public ReadingServlet(ReadingDAO readingDAO) {
        this.readingDAO = readingDAO;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        try {
            if (idParam == null) {
                response.setContentType("application/json");
                response.getWriter().write(readingDAO.getAllReadings().toString());
            } else {
                UUID id = UUID.fromString(idParam);
                Reading reading = readingDAO.getReadingById(UUID.fromString(idParam));
                if (reading != null) {
                    response.setContentType("application/json");
                    response.getWriter().write(reading.toString());
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Reading not found");
                }
            }
        } catch (SQLException e) {
            log("Database access error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerId = request.getParameter("customer_id");
        String type = request.getParameter("type");
        String reading = request.getParameter("reading");

        if (customerId == null || type == null || reading == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String customerId = request.getParameter("customer_id");
        String type = request.getParameter("type");
        String reading = request.getParameter("reading");

        if (idParam == null || customerId == null || type == null || reading == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            UUID id = UUID.fromString(idParam);
            Reading updatedReading = new Reading();
            if (readingDAO.updateReading(updatedReading)) {
                response.getWriter().write("Reading updated successfully");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Reading not found");
            }
        }
        catch (SQLException e) {
            log("Database access error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        try {
            readingDAO.deleteReading(UUID.fromString(id));
        } catch (SQLException e) {
            log("Database access error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");        }
    }
}