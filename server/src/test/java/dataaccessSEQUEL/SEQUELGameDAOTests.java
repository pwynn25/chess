package dataaccessSEQUEL;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.SequelAuthDAO;
import dataaccess.SequelGameDAO;
import exception.ExceptionResponse;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static service.UserService.generateToken;

public class SEQUELGameDAOTests extends dataaccessSQLAbstractClass{
    @Test
    @DisplayName("Successful Clear")
    public void clearSuccess() {
        GameDAO games = new SequelGameDAO();
        String gameName = "first game";
        try {
            games.createGame(gameName);
            games.clear();
            assertTrue(checkNumRows("AuthData"));
        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }

    @Test
    void createGameSuccess() {
        GameDAO games = new SequelGameDAO();
        String gameName = "first game";
        try {
            games.createGame(gameName);
            games.clear();
            assertNotEquals(0,checkNumRows("GameData"));
        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }

    @Test
    void createGameFailure() {

    }

    @Test
    void name() {
    }
}
