package handler;

import com.google.gson.Gson;
import exception.ExceptionResponse;
import request.ListRequest;
import request.RegisterRequest;
import result.ListResult;
import result.RegisterResult;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    GameService gameService;
    public ListGamesHandler(GameService gameService) {
        this.gameService = gameService;
    }
    public Object list(Request req, Response res) throws ExceptionResponse {
        String authToken = req.headers("authorization");

        ListResult result = gameService.list(authToken);
        res.status(200);

        return new Gson().toJson(result);
    }
}
