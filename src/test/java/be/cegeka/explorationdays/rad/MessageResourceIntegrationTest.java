package be.cegeka.explorationdays.rad;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import io.dropwizard.testing.junit.DropwizardAppRule;

import java.net.URI;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import be.cegeka.explorationdays.rad.representations.Message;

public class MessageResourceIntegrationTest {

    private static final Date NOW = new Date();
	@ClassRule
    public static final DropwizardAppRule<MessagewallConfiguration> RULE = new DropwizardAppRule<>(App.class, "config.yaml");
    private Client client;

    @Before
    public void setUp() {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
                .nonPreemptive().credentials("wsuser", "wspassword").build();
        client = ClientBuilder.newClient().register(feature);
    }

    @Test
    public void testCrudOperations() {
        Response response = doPOST(new Message(0, "testMessage", NOW));
        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        URI responsePOSTLocation = response.getLocation();
        response = doGET(responsePOSTLocation);
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.readEntity(Message.class).getContent()).isEqualTo("testMessage");

        response = doPUT(responsePOSTLocation, new Message(0, "updated testMessage"));
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        response = doGET(responsePOSTLocation);
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.readEntity(Message.class).getContent()).isEqualTo("updated testMessage"); //TODO test that the id of the message is not the id of the message in the json, but that of the saved message

        response = doDELETE(responsePOSTLocation);
        assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
        response = client.target(responsePOSTLocation).request().get();
        assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
    }

    private Response doDELETE(URI responsePOSTLocation) {
        return client.target(responsePOSTLocation).request().delete();
    }

    private Response doPUT(URI responsePOSTLocation, Message mess) {
        return client.target(responsePOSTLocation)
                .request()
                .put(Entity.json(mess));
    }

    private Response doGET(URI responsePOSTLocation) {
        return client.target(responsePOSTLocation).request().get();
    }

    private Response doPOST(Message message) {
        return client.target(String.format("http://localhost:%d/message", RULE.getLocalPort()))
                .request()
                .post(Entity.json(message));
    }
}
