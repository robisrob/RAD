package be.cegeka.explorationdays.rad;

import be.cegeka.explorationdays.rad.health.DatabaseHealthCheck;
import be.cegeka.explorationdays.rad.resources.MessageResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.CachingAuthenticator;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.skife.jdbi.v2.DBI;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

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
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowCredentials", "true");

        environment.healthChecks().register("Database health check", new DatabaseHealthCheck(jdbi));
    }
}
