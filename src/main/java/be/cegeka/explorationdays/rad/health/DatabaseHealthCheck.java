package be.cegeka.explorationdays.rad.health;

import be.cegeka.explorationdays.rad.dao.HealthCheckDAO;
import com.codahale.metrics.health.HealthCheck;
import org.skife.jdbi.v2.DBI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;

public class DatabaseHealthCheck extends HealthCheck{

    private final HealthCheckDAO healthCheckDAO;


    public DatabaseHealthCheck(DBI jdbi) {
        healthCheckDAO = jdbi.onDemand(HealthCheckDAO.class);
    }

    @Override
    protected Result check() throws Exception {
        if(healthCheckDAO.pingDatabase()) {
            return Result.healthy();
        }
        return Result.unhealthy("Can't ping database");
    }
}

