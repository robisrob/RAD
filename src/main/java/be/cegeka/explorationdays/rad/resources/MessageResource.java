package be.cegeka.explorationdays.rad.resources;

import be.cegeka.explorationdays.rad.dao.MessageDAO;
import be.cegeka.explorationdays.rad.representations.Message;
import org.skife.jdbi.v2.DBI;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

    private final MessageDAO messageDAO;

    public MessageResource(DBI jdbi) {
        this.messageDAO = jdbi.onDemand(MessageDAO.class);
    }

    @GET
    @Path("/{id}")
    public Response getMessage(@PathParam("id") int id) {
        Message message = messageDAO.getMessageById(id);
        return Response
                .ok(message)
                .build();
    }

    @POST
    public Response createMessage(Message message) throws URISyntaxException {
        int newMessageId = messageDAO.createMessage(message.getMessage());
        return Response.created(new URI("message/"+String.valueOf(newMessageId))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMessage(@PathParam("id") int id) {
        messageDAO.deleteMessage(id);
        return Response
                .noContent()
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateMessage(
            @PathParam("id") int id,
            Message message) {
        messageDAO.updateMessage(id, message.getMessage());
        return Response
                .ok(message)
                .build();
    }


}
