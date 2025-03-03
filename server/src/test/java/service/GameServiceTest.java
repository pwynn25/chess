package service;

import dataaccess.*;
import exception.ExceptionResponse;
import model.GameData;
import org.junit.jupiter.api.Test;
import request.ListRequest;
import request.RegisterRequest;
import result.ListResult;
import result.RegisterResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameServiceTest {

    @Test
     public void listGamesSuccess() throws ExceptionResponse {
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();
        GameDAO games = new MemoryGameDAO();

        String username1 = "pwynn25";
        String password1 = "pw264";
        String email1 = "jimmydean@gmail.com";

        String[] gameNames = {"losers","saucers","winners","sloppers"};
        int[] gameIDs = {1,2,3,4};

//        GameData game1 = new GameData(1, gameNames[0]);


        for(int i = 0; i < 4;i++) {
            games.createGame(gameNames[i]);
        }
        GameService gameService = new GameService(users, auths,games);
        UserService userService = new UserService(users,auths);

        RegisterRequest regReq = new RegisterRequest(username1,password1,email1);
        RegisterResult regResult = userService.register(regReq);

        ListRequest req = new ListRequest(regResult.authToken());

        ListResult listResult = gameService.list(req);








        // test getGames function: listGames





    }
}
