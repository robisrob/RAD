package be.cegeka.explorationdays.rad.resources.socket;

import java.io.IOException;

import javax.ws.rs.Path;

import org.atmosphere.cache.UUIDBroadcasterCache;
import org.atmosphere.client.TrackMessageSizeInterceptor;
import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.cpr.AtmosphereResponse;
import org.atmosphere.handler.OnMessage;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.interceptor.BroadcastOnPostAtmosphereInterceptor;
import org.atmosphere.interceptor.HeartbeatInterceptor;

import be.cegeka.explorationdays.rad.DBILookup;
import be.cegeka.explorationdays.rad.dao.MessageDAO;
import be.cegeka.explorationdays.rad.representations.Message;

import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/socket/")
@AtmosphereHandlerService(path = "/socket",
        broadcasterCache = UUIDBroadcasterCache.class,
        interceptors = {AtmosphereResourceLifecycleInterceptor.class,
                BroadcastOnPostAtmosphereInterceptor.class,
                TrackMessageSizeInterceptor.class,
                HeartbeatInterceptor.class
        })
public class MessageSocket extends OnMessage<String> {
    private final ObjectMapper mapper = new ObjectMapper();
	
    @Override
    public void onMessage(AtmosphereResponse response, String message) throws IOException {
        Message msg = mapper.readValue(message, Message.class);
        int id = messageDAO().createMessage(msg.getContent());
		response.write(mapper.writeValueAsString(messageDAO().getMessageById(id)));
    }

	private MessageDAO messageDAO() {
		return DBILookup.getInstance().getJdbi().onDemand(MessageDAO.class);
	}
}