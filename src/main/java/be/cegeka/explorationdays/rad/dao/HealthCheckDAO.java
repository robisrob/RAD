package be.cegeka.explorationdays.rad.dao;

import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface HealthCheckDAO {

    @SqlQuery("SELECT true FROM dual")
    boolean pingDatabase();

}
