package be.cegeka.explorationdays.rad.representations;

import io.dropwizard.validation.ValidationMethod;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Message {

    private final int id;
    @NotBlank
    @Length(min=2, max=255)
    private final String content;
    private final Date timestamp;

    public Message(){
        this(0, null, null);
    }

    public Message(String content) {
       this(0, content, null);
    }
    
    public Message(int id, String content, Date timestamp) {
    	this.id = id;
    	this.content = content;
    	this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
    
    public Date getTimestamp() {
		return timestamp;
	}
    
	@JsonIgnore
    @ValidationMethod(message="Message should not start with r")
    public boolean isValidMessage() {
        return !content.startsWith("r");
    }
}
