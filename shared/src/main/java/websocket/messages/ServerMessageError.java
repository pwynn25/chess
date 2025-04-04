package websocket.messages;

public class ServerMessageError extends ServerMessage{
    private String message;
    private ServerMessageType serverMessageType;

    public ServerMessageError(ServerMessageType serverMessageType) {
        this.serverMessageType = serverMessageType;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
