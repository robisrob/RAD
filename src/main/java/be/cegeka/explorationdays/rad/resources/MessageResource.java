package be.cegeka.explorationdays.rad.resources;

import be.cegeka.explorationdays.rad.dao.MessageDAO;
import be.cegeka.explorationdays.rad.representations.Message;
import io.dropwizard.auth.Auth;
import org.skife.jdbi.v2.DBI;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

    private final MessageDAO messageDAO;
    private final Validator validator;

    public MessageResource(DBI jdbi, Validator validator) {
        this.messageDAO = jdbi.onDemand(MessageDAO.class);
        this.validator = validator;
    }

    @GET
    @Path("/{id}")
    public Response getMessage(@PathParam("id") int id,@Auth Boolean isAuthenticated) {
        Message message = messageDAO.getMessageById(id);
        return message == null ? Response.noContent().build(): Response
                .ok(message)
                .build();
    }

    @POST
    public Response createMessage(@Valid Message message, @Auth Boolean isAuthenticated) throws URISyntaxException {
        int newMessageId = messageDAO.createMessage(message.getMessage());
        return Response.created(new URI("message/"+String.valueOf(newMessageId))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMessage(@PathParam("id") int id, @Auth Boolean isAuthenticated) {
        messageDAO.deleteMessage(id);
        return Response
                .noContent()
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateMessage(
            @PathParam("id") int id,
            Message message, @Auth Boolean isAuthenticated) {
        Set<ConstraintViolation<Message>> violations = validator.validate(message);
        if (violations.isEmpty()) {
            messageDAO.updateMessage(id, message.getMessage());
            return Response
                    .ok(new Message(id, message.getMessage()))
                    .build();
        } else {
            List<String> validationMessages = violations.stream().map(violation -> violation.getPropertyPath().toString() + ": " + violation.getMessage()).collect(Collectors.toList());
            return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
        }

    }


}
