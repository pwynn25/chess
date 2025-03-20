package handler;

import com.google.gson.Gson;
import exception.ExceptionResponse;
import request.JoinRequest;
import result.JoinResult;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    private GameService gameService;

    public JoinGameHandler(UserService userService, GameService gameService) {
        this.gameService = gameService;
    }
    public Object join(Request req, Response res) throws ExceptionResponse {
        JoinRequest request = new Gson().fromJson(req.body(), JoinRequest.class);
        String authToken = req.headers("authorization");

        JoinResult result = gameService.join(authToken,request);

        res.body(new Gson().toJson(result)); // this is redundant but good for conceptual understanding

        return new Gson().toJson(result);
    }

}
