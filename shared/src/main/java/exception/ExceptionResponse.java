package exception;

import com.google.gson.Gson;

import java.util.Map;

public class ExceptionResponse extends Exception{
     private int statusCode;
    private String message;

    public ExceptionResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
    public ExceptionResponse(Exception e) {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String toJson() {
        return new Gson().toJson((Map.of("message", getMessage(), "status",getStatusCode())));
    }
}
