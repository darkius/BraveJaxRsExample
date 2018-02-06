package lt.darkius.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Random;

/**
 * Created by darkius on 2018-01-30.
 */
@Path("/rest")
public class RestService {

    @Inject
    private PrefixService service;

    @GET
    public Response home() {
        return Response.status(200).entity("To say hi - append /{some-name} to current URL").build();
    }

    @GET
    @Path("/{name}")
    public Response printHi(@PathParam("name") String name) {
        String result = "Hi, " + service.getPrefix() + name;
        try {
            Thread.sleep(new Random().nextInt(5) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Response.status(200).entity(result).build();
    }

}
