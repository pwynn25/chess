package handler;

import com.google.gson.Gson;
import exception.ExceptionResponse;
import request.CreateRequest;
import request.ListRequest;
import result.CreateResult;
import service.GameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    GameService gameService;
    public CreateGameHandler(GameService gameService) {
        this.gameService = gameService;
    }
    public Object create(Request req, Response res) throws ExceptionResponse {
        CreateRequest request = new Gson().fromJson(req.body(), CreateRequest.class);
        String authToken = req.headers("authorization");
        CreateResult result = gameService.create(request,authToken);
        res.status(200);

        return new Gson().toJson(result);
    }
}
