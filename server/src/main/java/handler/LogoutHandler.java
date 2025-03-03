package handler;

import com.google.gson.Gson;
import exception.ExceptionResponse;
import request.LoginRequest;
import request.LogoutRequest;
import result.LoginResult;
import result.LogoutResult;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    private UserService userService;
    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }
    public Object logout(Request req, Response res) throws ExceptionResponse {
        String authToken = req.headers("authorization");
        LogoutRequest request = new LogoutRequest(authToken);
//                new Gson().fromJson(req.headers("authorization"), LogoutRequest.class);

        LogoutResult result = userService.logout(request);

        res.status(200);

        return new Gson().toJson(result);
    }
}
