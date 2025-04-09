package websocket.messages;

public class ErrorMessage extends ServerMessage{
    private String errorMessage;

    public ErrorMessage(ServerMessageType serverMessageType, String message) {
        super(serverMessageType);
        this.errorMessage = message;
    }
    public void setMessage(String message) {
        this.errorMessage = message;
    }

    public String getMessage() {
        return errorMessage;
    }
}
