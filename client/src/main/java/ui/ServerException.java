package ui;

public class ServerException extends Exception{
    private int statusCode;
    private String message;

    public ServerException(int statusCode,String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
