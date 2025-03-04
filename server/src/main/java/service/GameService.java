package service;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import exception.ExceptionResponse;
import model.AuthData;
import model.GameData;
import request.CreateRequest;
import request.JoinRequest;
import request.ListRequest;
import result.CreateResult;
import result.JoinResult;
import result.ListResult;


import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;


public class GameService {
    UserDAO users = new MemoryUserDAO();
    AuthDAO auths = new MemoryAuthDAO();
    GameDAO games = new MemoryGameDAO();

    public GameService(UserDAO users,AuthDAO auths, GameDAO games) {
        this.users = users;
        this.auths = auths;
        this.games = games;
    }
    public boolean isAuthorized (String authToken) {
        return auths.getAuth(authToken) != null;
    }


    public ListResult list(String authToken) throws ExceptionResponse{
        if(isAuthorized(authToken)) {
            // the user is authorized
            Collection<GameData> listOfGames = games.listGames();
            return new ListResult(listOfGames);
        }
        else {
            throw new ExceptionResponse(401,"Error: unauthorized");
        }
    }
    public CreateResult create(CreateRequest req, String authToken) throws ExceptionResponse{
        if(isAuthorized(authToken)) {
            // the user is authorized
            if(req.gameName() != null) {
                if(!req.gameName().isEmpty()) {
                    GameData newGame = games.createGame(req.gameName());
                    return new CreateResult(newGame.getGameID());
                }
                else {
                    throw new ExceptionResponse(400,"Error: bad request");
                }
            }
            else {
                throw new ExceptionResponse(400,"Error: bad request");
            }
        }
        else {
            throw new ExceptionResponse(401,"Error: unauthorized");
        }
    }
    public JoinResult join(String authToken, JoinRequest req) throws ExceptionResponse{
        if(isAuthorized(authToken)) {
            AuthData authData = auths.getAuth(authToken);
            String username = authData.getUsername();
            // bad request if the gameID does not exist
            GameData gameToJoin = games.getGame(req.gameID());
            if(gameToJoin == null) {
                throw new ExceptionResponse(400,"Error: bad request");
            }
            else {
                if(req.playerColor() == BLACK || req.playerColor() == WHITE) {
                    checkAndSet(req.playerColor(), username, gameToJoin);
                }
                else {
                    throw new ExceptionResponse(400,"Error: bad request");
                }
                // add player to game
                return new JoinResult(gameToJoin.getGameID());
            }
        }
        else {
            throw new ExceptionResponse(401,"Error: unauthorized");
        }
    }

    public void checkAndSet(ChessGame.TeamColor playerColor, String username, GameData game) throws ExceptionResponse{

        if(playerColor == WHITE) {
            if(isOccupied(game.getWhiteUsername())) {
                throw new ExceptionResponse(403,"Error: already taken");
            }
            else {
                updateGameHelper(playerColor, game.getGameID(), this.games,username);
            }

        }
        else {
            if(isOccupied(game.getBlackUsername())) {
                throw new ExceptionResponse(403,"Error: already taken");
            }
            else {
                updateGameHelper(playerColor, game.getGameID(), this.games,username);
            }
        }
    }

    public boolean isOccupied(String player) {
        return player != null;
    }
    public void updateGameHelper(ChessGame.TeamColor playerColor, int gameID, GameDAO games,String username) {
        if(playerColor == WHITE) {
            games.updateGameWhiteUsername(username,gameID);
        }
        else {
            games.updateGameBlackUsername(username,gameID);
        }
    }





}
