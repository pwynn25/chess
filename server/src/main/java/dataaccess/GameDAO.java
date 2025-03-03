package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear();
    GameData createGame(String gameName);
    ChessGame getGame(int gameID);
    Collection<GameData> listGames();
    void updateGame(String username);
}
