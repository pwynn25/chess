package websocket.messages;

public class ServerMessageError extends ServerMessage{
    private String message;

    public ServerMessageError(ServerMessageType serverMessageType, String message) {
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
