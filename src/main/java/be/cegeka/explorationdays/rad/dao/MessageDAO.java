package be.cegeka.explorationdays.rad.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import be.cegeka.explorationdays.rad.dao.mappers.MessageMapper;
import be.cegeka.explorationdays.rad.representations.Message;

public interface MessageDAO {

    @Mapper(MessageMapper.class)
    @SqlQuery("SELECT * FROM message where id = :id")
    Message getMessageById(@Bind("id") int id);
    
    @Mapper(MessageMapper.class)
    @SqlQuery("SELECT * FROM message order by timestamp desc")
    List<Message> getMessages();

    @GetGeneratedKeys
    @SqlUpdate("insert into message (id, content, timestamp) values (NULL, :content, now())")
    int createMessage(@Bind("content") String content);

    @SqlUpdate("update message set content = :content where id = :id")
    void updateMessage(@Bind("id") int id, @Bind("content") String content);

    @SqlUpdate("delete from message where id = :id")
    void deleteMessage(@Bind("id") int id);
}
