package dataaccess;

import chess.ChessGame;
import model.GameData;


import java.util.Collection;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{

    private Map<String, GameData> gameDataMap;


    void createGame(String gameID) {

    };
    ChessGame getGame(String gameID) {

    };
    Collection<ChessGame> listGames() {

    };
    void updateGame(String username) {

    };
    void joinGame(String username, String playerColor, String gameID) {

    };

}
