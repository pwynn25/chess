package service;

import dataaccess.*;
import exception.ExceptionResponse;
import model.AuthData;
import model.GameData;
import request.ListRequest;
import result.ListResult;
import result.LogoutResult;

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

    public ListResult list(ListRequest req) throws ExceptionResponse{
        AuthData authData = auths.getAuth(req.authToken());
        if(authData != null ) {
            // the user is authorized
            Collection<GameData> listOfGames = games.listGames();
            return new ListResult(listOfGames);
        }
        else {
            throw new ExceptionResponse(401,"Error: unauthorized");
        }
    }


}
