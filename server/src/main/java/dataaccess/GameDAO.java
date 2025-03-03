package dataaccess;

import chess.ChessGame;
import model.AuthData;

import java.util.Collection;

public interface GameDAO {
    void clear();
    void createGame(String gameName);
    ChessGame getGame(int gameID);
    Collection<Game> listGames();
    void updateGame(String username);
    Collection<AuthData>
}
