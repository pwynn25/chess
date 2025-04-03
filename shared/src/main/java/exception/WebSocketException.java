package exception;

public class WebSocketException extends Exception{
    private int statusCode;
    private String message;

    public WebSocketException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }


}
