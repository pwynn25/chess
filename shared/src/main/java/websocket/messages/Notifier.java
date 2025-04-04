package websocket.messages;

public class Notifier extends ServerMessage{
    private String message;
    ServerMessageType serverMessageType;

    public Notifier(ServerMessageType serverMessageType) {
        this.serverMessageType = serverMessageType;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
