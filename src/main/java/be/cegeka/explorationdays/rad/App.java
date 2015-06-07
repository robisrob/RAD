package be.cegeka.explorationdays.rad;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class App extends Application<Configuration>
{

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {
        new App().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        LOGGER.info("Method App#run() called");
        System.out.println( "Hello world, by Dropwizard!" );
    }
}
