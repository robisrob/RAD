package be.cegeka.explorationdays.rad;

import be.cegeka.explorationdays.rad.dao.UserDAO;
import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.skife.jdbi.v2.DBI;

public class MessageAuthenticator implements Authenticator<BasicCredentials, Boolean>{

    private final UserDAO userDAO;

    public MessageAuthenticator(DBI jdbi) {
        this.userDAO = jdbi.onDemand(UserDAO.class);
    }

    @Override
    public Optional<Boolean> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        if (userDAO.getUser(basicCredentials.getUsername(), basicCredentials.getPassword()) == 1) {
            return Optional.of(true);
        }
        return Optional.absent();
    }
}
