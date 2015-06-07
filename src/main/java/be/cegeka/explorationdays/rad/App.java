package be.cegeka.explorationdays.rad;

import be.cegeka.explorationdays.rad.resources.MessageResource;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class App extends Application<MessagewallConfiguration>
{

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {
        new App().run(args);
    }

    @Override
    public void run(MessagewallConfiguration configuration, Environment environment) throws Exception {
        LOGGER.info("Method App#run() called");
        environment.jersey().register(new MessageResource());
    }
}
