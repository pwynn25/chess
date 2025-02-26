package dataaccess;

import chess.ChessGame;
import model.UserData;

import java.util.Collection;

public interface UserDAO {
    void clear();
    UserData getUser(String username);
    void createUser(UserData userData);
}
