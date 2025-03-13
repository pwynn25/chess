package service;

import chess.ChessGame;
import dataaccess.*;
import exception.ExceptionResponse;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.CreateRequest;
import request.JoinRequest;
import request.RegisterRequest;
import result.CreateResult;
import result.ListResult;
import result.RegisterResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
   UserDAO users = new SequelUserDAO();
   AuthDAO auths = new SequelAuthDAO();
   GameDAO games = new SequelGameDAO();
   String validAuthTokenUser1;
   String validAuthTokenUser2;
   @BeforeEach
   public void setup() throws ExceptionResponse{
      String username1 = "pwynn25";
      String password1 = "pw264";
      String email1 = "jimmydean@gmail.com";

      String username2 = "brynn";
      String password2 = "Elizabeth";
      String email2 = "brynn@gmail.com";

      String username3 = "Nick";
      String password3 = "2601";
      String email3 = "debrah@gmail.com";

      RegisterRequest a = new RegisterRequest(username1, password1,email1);
      RegisterRequest b = new RegisterRequest(username2, password2,email2);
      RegisterRequest c = new RegisterRequest(username3, password3,email3);

      UserService userService = new UserService(users,auths);

      RegisterResult res1 = userService.register(a);
      this.validAuthTokenUser1 = res1.authToken();

      RegisterResult res2 = userService.register(b);
      this.validAuthTokenUser2 = res2.authToken();

      userService.register(c);

      Set<String> gameNames = new HashSet<>();
      gameNames.add("losers");
      gameNames.add("saucers");
      gameNames.add("winners");
      gameNames.add("sloppers");


      for(String gameName: gameNames) {
         games.createGame(gameName);
      }

   }
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
    @DisplayName("ListGameFailure")
    public void listGamesFailure() throws ExceptionResponse {
      GameService gameService = new GameService(users,auths,games);

      assertThrows(ExceptionResponse.class, () ->gameService.list("poop"));
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
//   join game success
   @Test
   @DisplayName("Join Game Success")
   public void joinGameSuccess() throws ExceptionResponse {
      UserService userService = new UserService(users,auths);
      GameService gameService = new GameService(users,auths,games);

      int gameID = 1;

      JoinRequest joinRequest = new JoinRequest(gameID, ChessGame.TeamColor.WHITE);

      gameService.join(validAuthTokenUser1,joinRequest);

      assertEquals("pwynn25",games.getGame(gameID).getWhiteUsername());

   }


//    Join game failure
@Test
@DisplayName("Join Game Success")
public void joinGameFailure() throws ExceptionResponse {
   UserService userService = new UserService(users,auths);
   GameService gameService = new GameService(users,auths,games);

   int gameID = 1;

   JoinRequest joinRequest = new JoinRequest(gameID, ChessGame.TeamColor.WHITE);

   gameService.join(validAuthTokenUser1,joinRequest);

   assertEquals("pwynn25",games.getGame(gameID).getWhiteUsername());

   JoinRequest joinRequestFail = new JoinRequest(gameID, ChessGame.TeamColor.WHITE);

   assertThrows(ExceptionResponse.class, () ->gameService.join(validAuthTokenUser1,joinRequestFail));
}


}
