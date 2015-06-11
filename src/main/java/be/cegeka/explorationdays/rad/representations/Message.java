package be.cegeka.explorationdays.rad.representations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dropwizard.validation.ValidationMethod;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class Message {

    private final int id;
    @NotBlank
    @Length(min=2, max=255)
    private final String message;

    public Message(){
        this(0, null);
    }

    public Message(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @JsonIgnore
    @ValidationMethod(message="Message should not start with r")
    public boolean isValidMessage() {
        return message.startsWith("r") ? false: true;
    }
}
