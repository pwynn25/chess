package handler;

import com.google.gson.Gson;
import exception.ExceptionResponse;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import service.UserService;
import spark.Request;
import spark.Response;

public class LoginHandler {
    private UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }
    public Object login(Request req, Response res) throws ExceptionResponse {
        LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);

        LoginResult result = userService.login(request);
        res.header("authorization",result.authToken());

        res.status(200);
        // the return value from the handler automatically becomes the body
        return new Gson().toJson(result);
    }
}
