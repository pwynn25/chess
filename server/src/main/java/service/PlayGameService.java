package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ExceptionResponse;
import exception.WebSocketException;
import model.AuthData;
import model.GameData;
import request.LeaveRequest;
import request.ResignRequest;
import result.LeaveResult;
import result.ResignResult;

import java.util.Objects;

public class PlayGameService {
    UserDAO users;
    AuthDAO auths;
    GameDAO games;

    public PlayGameService(UserDAO users,AuthDAO auths, GameDAO games) {
        this.users = users;
        this.auths = auths;
        this.games = games;
    }
    public boolean isAuthorized (String authToken) throws ExceptionResponse {
        return auths.getAuth(authToken) != null;
    }
    // connect


    // makeMove


    // leaveGame
    public LeaveResult leave(LeaveRequest req) throws WebSocketException{
        String message;
        try {
            AuthData auth = auths.getAuth(req.authToken());
            GameData game = games.getGame(req.gameID());
            if (isAuthorized(req.authToken())) {
                if(Objects.equals(game.getBlackUsername(), auth.getUsername())) {
                    games.updateGameBlackUsername(null, req.gameID());
                }
                else if (Objects.equals(game.getWhiteUsername(), auth.getUsername())) {
                    games.updateGameWhiteUsername(null, req.gameID());
                }
            }
            message = auth.getUsername() + " has left the game.";
            return new LeaveResult(games.getGame(req.gameID()),message);
        } catch (ExceptionResponse e) {
            throw new WebSocketException(e.getStatusCode(),e.getMessage());
        }
    }
    // resignGame

    public ResignResult resign(ResignRequest request) throws WebSocketException {
        String message;
        try {
            AuthData auth = auths.getAuth(request.authToken());
            GameData game = games.getGame(request.gameID());
            if (isAuthorized(request.authToken())) {
                if(Objects.equals(game.getBlackUsername(), auth.getUsername())) {
                    games.updateGameBlackUsername(null, request.gameID());
                }
                else if (Objects.equals(game.getWhiteUsername(), auth.getUsername())) {
                    games.updateGameWhiteUsername(null, request.gameID());
                }
            }
            message = auth.getUsername() + " has left the game.";
            return new LeaveResult(games.getGame(request.gameID()),message);


        } catch (ExceptionResponse e) {
            throw new WebSocketException(e.getStatusCode(),e.getMessage());
        }
    }



}
