package be.cegeka.explorationdays.rad.dao.mappers;

import be.cegeka.explorationdays.rad.representations.Message;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements ResultSetMapper<Message> {

    @Override
    public Message map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Message(resultSet.getInt("id"), resultSet.getString("message"));
    }
}
