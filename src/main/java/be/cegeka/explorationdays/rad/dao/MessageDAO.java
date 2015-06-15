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
    @SqlUpdate("insert into message (id, content, timestamp) values (NULL, :content, now())")
    int createMessage(@Bind("content") String content);

    @SqlUpdate("update message set content = :content where id = :id")
    void updateMessage(@Bind("id") int id, @Bind("content") String content);

    @SqlUpdate("delete from message where id = :id")
    void deleteMessage(@Bind("id") int id);
}
