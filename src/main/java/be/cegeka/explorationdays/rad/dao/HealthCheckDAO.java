package be.cegeka.explorationdays.rad.dao;

import be.cegeka.explorationdays.rad.representations.Message;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * Created by rob on 13/06/15.
 */
public interface HealthCheckDAO {

    @SqlQuery("SELECT true FROM dual")
    boolean pingDatabase();

}
