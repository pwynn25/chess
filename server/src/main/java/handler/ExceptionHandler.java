package handler;

import exception.ExceptionResponse;
import spark.Request;
import spark.Response;

public class ExceptionHandler {



    public void handleException(ExceptionResponse exResponse, Request req, Response res) {
        res.status(exResponse.getStatusCode());

        res.body(exResponse.toJson());
    }
}
