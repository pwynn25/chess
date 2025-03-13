package dataaccess;

import chess.ChessGame;
import exception.ExceptionResponse;
import model.UserData;

import java.util.Collection;

public interface UserDAO {
    void clear();
    UserData getUser(String username) throws ExceptionResponse;
    void createUser(UserData userData) throws ExceptionResponse;
}
