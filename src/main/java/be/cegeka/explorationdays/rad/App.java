package be.cegeka.explorationdays.rad;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.CachingAuthenticator;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;

import javax.servlet.ServletRegistration;

import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.cegeka.explorationdays.rad.health.DatabaseHealthCheck;
import be.cegeka.explorationdays.rad.resources.MessageResource;
import be.cegeka.explorationdays.rad.resources.socket.MessageSocket;

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
        DataSourceFactory database = configuration.getDatabase();
        final DBI jdbi = factory.build(environment, database, "mysql");
        CachingAuthenticator<BasicCredentials, Boolean> authenticator = new CachingAuthenticator<>(environment.metrics(), new MessageAuthenticator(jdbi), configuration.getAuthenticationCachePolicy());
        environment.jersey().register(new MessageResource(jdbi, environment.getValidator()));
        environment.jersey().register(AuthFactory.binder(new BasicAuthFactory<>(authenticator, "Webservice Authentication", Boolean.class)));

        environment.healthChecks().register("Database health check", new DatabaseHealthCheck(jdbi));
        
        
        AtmosphereServlet servlet = new AtmosphereServlet();
        servlet.framework().addInitParameter(ApplicationConfig.ANNOTATION_PACKAGE, MessageSocket.class.getPackage().getName());
        servlet.framework().addInitParameter(ApplicationConfig.WEBSOCKET_CONTENT_TYPE, "application/json");
        servlet.framework().addInitParameter(ApplicationConfig.WEBSOCKET_SUPPORT, "true");
         
        ServletRegistration.Dynamic servletHolder = environment.servlets().addServlet("MessageSocket", servlet);
        servletHolder.addMapping("/socket/*");
    }
}
