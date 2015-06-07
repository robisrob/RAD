package be.cegeka.explorationdays.rad.resources;

import be.cegeka.explorationdays.rad.representations.Message;

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
                .ok(new Message(id, "dummy message"))
                .build();
    }

    @POST
    public Response createMessage(Message message) {
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
            Message message) {
        return Response
                .ok(message)
                .build();
    }


}
