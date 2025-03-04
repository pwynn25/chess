package service;

import dataaccess.*;
import exception.ExceptionResponse;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.CreateRequest;
import request.ListRequest;
import request.RegisterRequest;
import result.CreateResult;
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

        String authToken = regResult.authToken();
        ListResult listResult = gameService.list(authToken);

        Collection <GameData> updatedGames = listResult.games();

        for(GameData game: updatedGames) {
           assertTrue(gameNames.contains(game.getGameName()));
        }
    }
    @Test
    @DisplayName("Create Game Success")
    public void createGameSuccess() throws ExceptionResponse {
       UserDAO users = new MemoryUserDAO();
       AuthDAO auths = new MemoryAuthDAO();
       GameDAO games = new MemoryGameDAO();

       String username1 = "pwynn";
       String password1 = "jimbob";
       String email1 = "jimmybob@gmail.com";
       UserService userService = new UserService(users,auths);

       RegisterResult regRes = userService.register(new RegisterRequest(username1,password1,email1));
       GameService gameService = new GameService(users,auths,games);

       CreateRequest createRequest1 = new CreateRequest("losers");
       CreateRequest createRequest2 = new CreateRequest("dufus");
       CreateRequest createRequest3 = new CreateRequest("losers");
       CreateRequest createRequest4 = new CreateRequest("yuhhhhh");

       CreateResult createResult1 = gameService.create(createRequest1, regRes.authToken());
       CreateResult createResult2 = gameService.create(createRequest2, regRes.authToken());
       CreateResult createResult3 = gameService.create(createRequest3, regRes.authToken());
       CreateResult createResult4 = gameService.create(createRequest4, regRes.authToken());

       // find all the new games
       assertNotNull(games.getGame(createResult1.gameID()));
       assertNotNull(games.getGame(createResult2.gameID()));
       assertNotNull(games.getGame(createResult3.gameID()));
       assertNotNull(games.getGame(createResult4.gameID()));
    }

   @Test
   @DisplayName("Create Game Failure")
   public void createGameFailure() throws ExceptionResponse {
      UserDAO users = new MemoryUserDAO();
      AuthDAO auths = new MemoryAuthDAO();
      GameDAO games = new MemoryGameDAO();

      String username1 = "pwynn";
      String password1 = "jimbob";
      String email1 = "jimmybob@gmail.com";
      UserService userService = new UserService(users,auths);

      RegisterResult regRes = userService.register(new RegisterRequest(username1,password1,email1));
      GameService gameService = new GameService(users,auths,games);

      CreateRequest createRequest1 = new CreateRequest("losers");
      CreateRequest createRequest2 = new CreateRequest("yuhh");
      CreateRequest createRequest3 = new CreateRequest("losers");
      CreateRequest createRequest4 = new CreateRequest("");


      try {
         CreateResult createResult1 = gameService.create(createRequest1, regRes.authToken());
         CreateResult createResult2 = gameService.create(createRequest2, regRes.authToken());
         CreateResult createResult3 = gameService.create(createRequest3, regRes.authToken());
         CreateResult createResult4 = gameService.create(createRequest4, regRes.authToken());
         fail();
      } catch (ExceptionResponse e) {
         assertEquals(400,e.getStatusCode());
         assertEquals("Error: bad request",e.getMessage());
      }
   }
}
