package handler;

import exception.ExceptionResponse;
import exception.ExceptionResponseNoThrow;
import spark.Request;
import spark.Response;

public class ExceptionHandler {


    public void handleException(ExceptionResponse exResponse, Request req, Response res) {
        ExceptionResponseNoThrow exceptionResponseNoThrow = new ExceptionResponseNoThrow(exResponse);
        res.status(exceptionResponseNoThrow.getStatusCode());

        res.body(exceptionResponseNoThrow.toJson());
    }
}
