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

    public GameData createGame(String gameName) {

        GameData newGame = new GameData(gameID++, gameName);
        gameDataMap.put(newGame.getGameID(), newGame);
        return newGame;
    };
    public GameData getGame(int gameID) {
        return gameDataMap.get(gameID);
    };
    public Collection<GameData> listGames() {
        return gameDataMap.values();
    };
    public void updateGameWhiteUsername(String username, int gameID) {
        gameDataMap.get(gameID).setWhiteUsername(username);
    };
    public void updateGameBlackUsername (String username, int gameID) {
        gameDataMap.get(gameID).setBlackUsername(username);
    }
    public void clear() {
        gameDataMap.clear();
    }
    public void updateGame(int gameID, ChessGame game) {

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
