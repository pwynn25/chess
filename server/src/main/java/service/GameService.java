package service;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ExceptionResponse;
import model.AuthData;
import model.GameData;
import request.CreateRequest;
import request.ListRequest;
import result.CreateResult;
import result.ListResult;
import result.LogoutResult;
import spark.Request;
import spark.Response;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class GameService {
    UserDAO users = new MemoryUserDAO();
    AuthDAO auths = new MemoryAuthDAO();
    GameDAO games = new MemoryGameDAO();

    public GameService(UserDAO users,AuthDAO auths, GameDAO games) {
        this.users = users;
        this.auths = auths;
        this.games = games;
    }
    public boolean isAuthorized (String authToken) {
        return auths.getAuth(authToken) != null;
    }


    public ListResult list(ListRequest req) throws ExceptionResponse{
        if(isAuthorized(req.authToken())) {
            // the user is authorized
            Collection<GameData> listOfGames = games.listGames();
            return new ListResult(listOfGames);
        }
        else {
            throw new ExceptionResponse(401,"Error: unauthorized");
        }
    }
    public CreateResult create(CreateRequest req) throws ExceptionResponse{
        if(isAuthorized(req.authToken())) {
            // the user is authorized
            GameData newGame = games.createGame(req.gameName());
            return new CreateResult(newGame.getGameID());
        }
        else {
            throw new ExceptionResponse(401,"Error: unauthorized");
        }
    }





}
