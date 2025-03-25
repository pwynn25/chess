package exception;

import com.google.gson.Gson;

import java.util.Map;

public class ExceptionResponseNoThrow {
    private String message;
    private int statusCode;

    public ExceptionResponseNoThrow(ExceptionResponse ex) {
        this.statusCode = ex.getStatusCode();
        this.message = ex.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String toJson() {
        return new Gson().toJson((Map.of("message", getMessage())));
    }
}
