package dataaccess;

import chess.ChessGame;
import model.GameData;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO{

    private Map< Integer, GameData> gameDataMap = new HashMap<>();
    private int gameID = 1;

    public void createGame(String gameName) {

        GameData newGame = new GameData(gameID++, gameName);
        gameDataMap.put(newGame.getGameID(), newGame);
    };
    public ChessGame getGame(int gameID) {
        return null;
    };
    public Collection<GameData> listGames() {
        return gameDataMap.values();
    };
    public void updateGame(String username) {

    };
    void joinGame(String username, String playerColor, String gameID) {

    };
    public void clear() {
        gameDataMap.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemoryGameDAO that = (MemoryGameDAO) o;
        return gameID == that.gameID && Objects.equals(gameDataMap, that.gameDataMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameDataMap, gameID);
    }
}
