package be.cegeka.explorationdays.rad.representations;

public class Message {

    private final int id;
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
}
