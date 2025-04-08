package websocket.messages;

public class Notifier extends ServerMessage{
    private String message;

    public Notifier(ServerMessageType serverMessageType, String message) {
        super(serverMessageType);
        this.message = message;

    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
