package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear();
    GameData createGame(String gameName);
    GameData getGame(int gameID);
    Collection<GameData> listGames();
    public void updateGameWhiteUsername(String username, int gameID);
    public void updateGameBlackUsername (String username, int gameID);
}
