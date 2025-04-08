package dataaccess;

import chess.ChessGame;
import exception.ExceptionResponse;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear() throws ExceptionResponse;
    GameData createGame(String gameName) throws ExceptionResponse;
    GameData getGame(int gameID) throws ExceptionResponse;
    Collection<GameData> listGames() throws ExceptionResponse;
    void updateGameWhiteUsername(String username, int gameID) throws ExceptionResponse;
    void updateGameBlackUsername (String username, int gameID) throws ExceptionResponse;
    void updateGame(int gameID, ChessGame game) throws ExceptionResponse;
}
