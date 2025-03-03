package service;

import dataaccess.*;
import exception.ExceptionResponse;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.ListRequest;
import request.RegisterRequest;
import result.ListResult;
import result.RegisterResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    @Test
     public void listGamesSuccess() throws ExceptionResponse {
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();
        GameDAO games = new MemoryGameDAO();

        String username1 = "pwynn25";
        String password1 = "pw264";
        String email1 = "jimmydean@gmail.com";


        Set<String> gameNames = new HashSet<>();
        gameNames.add("losers");
        gameNames.add("saucers");
        gameNames.add("winners");
        gameNames.add("sloppers");


        for(String gameName: gameNames) {
           games.createGame(gameName);
        }
        GameService gameService = new GameService(users, auths,games);
        UserService userService = new UserService(users,auths);

        RegisterRequest regReq = new RegisterRequest(username1,password1,email1);
        RegisterResult regResult = userService.register(regReq);

        ListRequest req = new ListRequest(regResult.authToken());

        ListResult listResult = gameService.list(req);

        Collection <GameData> updatedGames = listResult.games();

        for(GameData game: updatedGames) {
           assertTrue(gameNames.contains(game.getGameName()));
        }
    }
    @Test
    @DisplayName("No current games exist")
    public void listGamesFailure() throws ExceptionResponse {

    }
}
