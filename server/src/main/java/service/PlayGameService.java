package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ExceptionResponse;
import exception.WebSocketException;
import model.AuthData;
import model.GameData;
import request.ConnectRequest;
import request.LeaveRequest;
import request.ResignRequest;
import result.ConnectResult;
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
    public ConnectResult connect(ConnectRequest connectRequest) throws WebSocketException {
        try {
            AuthData auth = auths.getAuth(connectRequest.authToken());
            GameData game = games.getGame(connectRequest.gameID());
            if(isAuthorized(connectRequest.authToken())) {
                game = games.getGame(connectRequest.gameID());
            }
            return new ConnectResult(auth.getUsername(), game);
        }catch (ExceptionResponse e) {
            throw new WebSocketException(e.getStatusCode(),e.getMessage());
        }
    }

    // makeMove


    // leaveGame
    public LeaveResult leave(LeaveRequest req) throws WebSocketException{
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
            return new LeaveResult(games.getGame(req.gameID()),auth.getUsername());
        } catch (ExceptionResponse e) {
            throw new WebSocketException(e.getStatusCode(),e.getMessage());
        }
    }


    // resignGame
    public ResignResult resign(ResignRequest request) throws WebSocketException {
        String winner = "";
        try {
            AuthData auth = auths.getAuth(request.authToken());
            GameData game = games.getGame(request.gameID());
            if (isAuthorized(request.authToken())) {
                if(Objects.equals(game.getBlackUsername(), auth.getUsername())) {
                    games.updateGame(request.gameID(),false);
                    winner = game.getWhiteUsername();
                }
                else if (Objects.equals(game.getWhiteUsername(), auth.getUsername())) {
                    games.updateGame(request.gameID(),false);
                    winner = game.getBlackUsername();
                }
            }
            return new ResignResult(games.getGame(request.gameID()),winner);
        } catch (ExceptionResponse e) {
            throw new WebSocketException(e.getStatusCode(),e.getMessage());
        }
    }



}
