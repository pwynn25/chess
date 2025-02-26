package dataaccess;

import chess.ChessGame;

import java.util.Collection;

public interface GameDAO {
    void clear();
    void createGame(String gameID);
    ChessGame getGame(String gameID);
    Collection<ChessGame> listGames();
    void updateGame(String username);

}
