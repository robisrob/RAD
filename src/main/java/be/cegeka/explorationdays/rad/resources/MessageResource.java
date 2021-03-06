package be.cegeka.explorationdays.rad.resources;

import io.dropwizard.auth.Auth;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;

import be.cegeka.explorationdays.rad.dao.MessageDAO;
import be.cegeka.explorationdays.rad.representations.Message;

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
    public Response getMessage(@PathParam("id") int id) {
        Message message = messageDAO.getMessageById(id);
        return message == null ? Response.noContent().build(): Response
                .ok(message)
                .build();
    }
    
    @GET
    public Response getMessages(@Auth Boolean isAuthenticated) {
    	GenericEntity<List<Message>> messages = new GenericEntity<List<Message>>(messageDAO.getMessages()){};
    	return messages.getEntity().isEmpty() ? Response.noContent().build() : Response.ok(messages).build();
    }

    @POST
    public Response createMessage(@Valid Message message) throws URISyntaxException {
        int newMessageId = messageDAO.createMessage(message.getContent());
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
        Set<ConstraintViolation<Message>> violations = validator.validate(message);
        if (violations.isEmpty()) {
            messageDAO.updateMessage(id, message.getContent());
            Message msg = messageDAO.getMessageById(id);
            return Response
                    .ok(new Message(id, message.getContent(), msg.getTimestamp()))
                    .build();
        } else {
            List<String> validationMessages = violations.stream().map(violation -> violation.getPropertyPath().toString() + ": " + violation.getMessage()).collect(Collectors.toList());
            return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
        }

    }


}
