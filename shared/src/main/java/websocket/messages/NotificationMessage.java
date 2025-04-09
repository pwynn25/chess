package websocket.messages;

public class NotificationMessage extends ServerMessage{
    private String message;

    public NotificationMessage(ServerMessageType serverMessageType, String message) {
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
