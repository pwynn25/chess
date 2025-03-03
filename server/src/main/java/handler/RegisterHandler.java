package handler;

import com.google.gson.Gson;
import exception.ExceptionResponse;
import request.RegisterRequest;
import result.RegisterResult;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    private UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }
    public Object register(Request req,Response res) throws ExceptionResponse {
        RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);

        RegisterResult result = userService.register(request);
        res.status(200);

        return new Gson().toJson(result);
    }
}
