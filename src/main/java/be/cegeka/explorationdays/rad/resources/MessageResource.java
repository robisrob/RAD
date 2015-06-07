package be.cegeka.explorationdays.rad.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

    @GET
    @Path("/{id}")
    public Response getMessage(@PathParam("id") int id) {
        return Response
                .ok("{message_id: " + id + ", message: \"Dummy message\"}")
                .build();
    }

    @POST
    public Response createMessage(@FormParam("name") String name) {
        return Response.created(null).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMessage(@PathParam("id") int id) {
        return Response
                .noContent()
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateMessage(
            @PathParam("id") int id,
            @FormParam("message") String message) {
        return Response
                .ok("{message_id: " + id + ", message: \"" + message + "\"}")
                .build();
    }


}
