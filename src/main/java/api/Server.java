package api;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.grizzly.http.server.HttpServer;

import java.net.URI;

public class Server {

    private HttpServer server;

    public void startServer() {
        try {
            String url = "http://localhost:8080/";
            String pack = "api";
            ResourceConfig config = new ResourceConfig();
            config.packages(pack);
            server = GrizzlyHttpServerFactory.createHttpServer(URI.create(url), config);
            server.start();
            System.out.println("Server started at: " + url);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error starting the server.");
        }
    }

    public void stopServer() {
        if (server != null) {
            try {
                server.stop();
                System.out.println("Server stopped.");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error stopping the server.");
            }
        }
    }
}
