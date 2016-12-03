package ch.epfl.sweng.project.chat;

/**
 *  Class representing the message in the char.
 */
public class Message {
    private final String userName;
    private final String body;
    private final long time;

    /**
     * Constructor of the class
     *
     * @param userName Name of the user which sends the message
     * @param body Body of the message
     * @param time time when the message was sent
     */
    public Message(String userName, String body, long time) {
        this.userName = userName;
        this.body = body;
        this.time = time;
    }

    /**
     * Getter that returns the name of the user which sends the message
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Getter that returns the body of the message
     */
    public String getBody() {
        return body;
    }

    /**
     * Getter that returns the time when the message was sent
     */
    public long getTime() {
        return time;
    }

}
