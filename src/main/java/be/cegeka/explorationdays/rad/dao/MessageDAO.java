package be.cegeka.explorationdays.rad.dao;

import be.cegeka.explorationdays.rad.dao.mappers.MessageMapper;
import be.cegeka.explorationdays.rad.representations.Message;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface MessageDAO {

    @Mapper(MessageMapper.class)
    @SqlQuery("SELECT * FROM message where id = :id")
    Message getMessageById(@Bind("id") int id);


    @GetGeneratedKeys
    @SqlUpdate("insert into message (id, message) values (NULL, :message)")
    int createMessage(@Bind("message") String message);

    @SqlUpdate("update message set message = :message where id = :id")
            void updateMessage(@Bind("id") int id, @Bind("message")
            String message);

    @SqlUpdate("delete from message where id = :id")
    void deleteMessage(@Bind("id") int id);
}
