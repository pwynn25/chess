package service;

import dataaccess.*;
import exception.ExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ClearServiceTest {
    UserDAO users = new MemoryUserDAO();
    AuthDAO auths = new MemoryAuthDAO();
    GameDAO games = new MemoryGameDAO();
    @BeforeEach
    public void setup() throws ExceptionResponse{
        String username1 = "pwynn25";
        String password1 = "pw264";
        String email1 = "jimmydean@gmail.com";

        String username20 = "brynn";
        String password20 = "Elizabeth";
        String email20 = "brynn@gmail.com";

        String username3 = "Nick";
        String password3 = "2601";
        String email3 = "debrah@gmail.com";

        RegisterRequest a = new RegisterRequest(username1, password1,email1);
        RegisterRequest b = new RegisterRequest(username20, password20,email20);
        RegisterRequest c = new RegisterRequest(username3, password3,email3);

        UserService userService = new UserService(users,auths);

        userService.register(a);
        userService.register(b);
        userService.register(c);

        Set<String> gameNames = new HashSet<>();
        gameNames.add("losers");
        gameNames.add("tossers");
        gameNames.add("stoppers");
        gameNames.add("sloppers");


        for(String gameName: gameNames) {
            games.createGame(gameName);
        }

    }

    @Test
    @DisplayName("Successful Clear")
    public void clearSuccess() {
        ClearService clearService = new ClearService(users,auths,games);
        clearService.clear();

        assertNull(games.getGame(1));
        assertNull(users.getUser("Nick"));
    }

}
