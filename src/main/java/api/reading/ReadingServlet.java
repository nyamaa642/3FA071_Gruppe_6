package api.reading;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet("/readings")
public class ReadingServlet extends HttpServlet {

    private final ReadingDAO readingDAO;

    public ReadingServlet(ReadingDAO readingDAO) {
        this.readingDAO = readingDAO;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        try {
            if (id != null) {
                Reading reading = readingDAO.getReadingById(UUID.fromString(id));
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(reading);
                resp.setContentType("application/json");
                resp.getWriter().write(json);
            } else {
                List<Reading> readings = readingDAO.getAllReadings();
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(readings);
                resp.setContentType("application/json");
                resp.getWriter().write(json);
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            Reading reading = objectMapper.readValue(json, Reading.class);
            readingDAO.addReading(reading);

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException | IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            Reading reading = objectMapper.readValue(json, Reading.class);

            readingDAO.updateReading(reading);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException | IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        try {
            readingDAO.deleteReading(UUID.fromString(id));
        } catch (SQLException e) {
            //TODO Handle the exception, e.g., send an error response
        }
    }
}