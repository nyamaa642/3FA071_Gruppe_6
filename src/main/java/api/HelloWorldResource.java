package api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("helloworld")
public class HelloWorldResource {

    @GET
    public String getHelloWorld(){
        return "Hello World!";
    }

}
