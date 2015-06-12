package be.cegeka.explorationdays.rad;

import be.cegeka.explorationdays.rad.resources.MessageResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.CachingAuthenticator;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
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
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDatabase(), "mysql");
        CachingAuthenticator<BasicCredentials, Boolean> authenticator = new CachingAuthenticator<>(environment.metrics(), new MessageAuthenticator(jdbi), configuration.getAuthenticationCachePolicy());
        environment.jersey().register(new MessageResource(jdbi, environment.getValidator()));
        environment.jersey().register(AuthFactory.binder(new BasicAuthFactory<>(authenticator, "Webservice Authentication", Boolean.class)));
    }
}
