package service;

import chess.ChessBoard;
import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ExceptionResponse;
import exception.WebSocketException;
import model.AuthData;
import model.GameData;
import request.ConnectRequest;
import request.LeaveRequest;
import request.MoveRequest;
import request.ResignRequest;
import result.ConnectResult;
import result.LeaveResult;
import result.MoveResult;
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
            GameData game;
            if(isAuthorized(connectRequest.authToken())) {
                game = games.getGame(connectRequest.gameID());
                if (game == null) {
                    throw new WebSocketException(500, "Error: invalid game ID");
                }
                return new ConnectResult(auth.getUsername(), game);
            }
            else {
                throw new WebSocketException(500, "Error: unauthorized");
            }
        }catch (ExceptionResponse e) {
            throw new WebSocketException(e.getStatusCode(),e.getMessage());
        }
    }


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
            checkIfActive(game);

            ChessGame finishedGame = game.getGame();
            if (isAuthorized(request.authToken())) {
                finishedGame.setStatus(false);
                if(Objects.equals(game.getBlackUsername(), auth.getUsername())) {
                    games.updateGame(request.gameID(),finishedGame);
                    winner = game.getWhiteUsername();
                }
                else if (Objects.equals(game.getWhiteUsername(), auth.getUsername())) {
                    games.updateGame(request.gameID(), finishedGame);
                    winner = game.getBlackUsername();
                }
                else {
                    throw new WebSocketException(500, "an observer cannot resign from a game");
                }
            }
            return new ResignResult(games.getGame(request.gameID()),winner,auth.getUsername());
        } catch (ExceptionResponse e) {
            throw new WebSocketException(e.getStatusCode(),e.getMessage());
        }
    }

    public MoveResult makeMove(MoveRequest request) throws WebSocketException {
        try {
            AuthData auth = auths.getAuth(request.authToken());
            GameData gameData = games.getGame(request.gameID());
            checkIfActive(gameData);
            if (isAuthorized(request.authToken())) {
                ChessGame.TeamColor reqPlayerColor = getReqPLayerColor(gameData,auth.getUsername());
                ChessGame game = gameData.getGame();
                if (game.getTeamTurn() == reqPlayerColor) {
                    try {
                        game.makeMove(request.move());
                        games.updateGame(gameData.getGameID(), game);
                        return new MoveResult(games.getGame(gameData.getGameID()));
                    }
                    catch (InvalidMoveException e) {
                        throw new WebSocketException(500, e.getMessage());
                    }
                }
                else {
                    throw new WebSocketException(500, "Error: " + auth.getUsername() + " tried to move out of turn");
                }
            }
            else {
                throw new WebSocketException(500,"Error: unauthorized");
            }
        } catch (ExceptionResponse e) {
            throw new WebSocketException(e.getStatusCode(),e.getMessage());
        }
    }

    public void checkIfActive(GameData gameData) throws WebSocketException{
        if(!gameData.getGameStatus()) {
            throw new WebSocketException(500, "the game is over");
        }
    }

    public ChessGame.TeamColor getReqPLayerColor(GameData gameData, String username) throws WebSocketException{
        ChessGame.TeamColor reqPlayerColor;
        if(Objects.equals(gameData.getBlackUsername(), username)) {
            reqPlayerColor = ChessGame.TeamColor.BLACK;
        }
        else if (Objects.equals(gameData.getWhiteUsername(), username)) {
            reqPlayerColor = ChessGame.TeamColor.WHITE;
        }
        else {
            throw new WebSocketException(500,"Error: " + username + "is an observer");
        }
        return reqPlayerColor;
    }



}
