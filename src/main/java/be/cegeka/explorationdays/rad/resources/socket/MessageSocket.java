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
import org.eclipse.jetty.server.Authentication.User;

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
        response.write(message);
//        response.write(mapper.writeValueAsString(mapper.readValue(message, String.class)));
    }
}