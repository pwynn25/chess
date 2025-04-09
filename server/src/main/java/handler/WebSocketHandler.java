package handler;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.WebSocketException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import request.ConnectRequest;
import request.LeaveRequest;
import request.MoveRequest;
import request.ResignRequest;
import result.ConnectResult;
import result.LeaveResult;
import result.MoveResult;
import result.ResignResult;
import service.PlayGameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.messages.ErrorMessage;

import java.io.IOException;

import java.util.Objects;
import java.util.Set;

@WebSocket
public class WebSocketHandler {
    public PlayGameService playGameService;
    public WebSocketSessions webSocketSessions;


    public WebSocketHandler(PlayGameService playGameService, WebSocketSessions webSocketSessions) {
        this.playGameService = playGameService;
        this.webSocketSessions = webSocketSessions;

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand userGameCommand = new  Gson().fromJson(message, UserGameCommand.class);

        int gameID = userGameCommand.getGameID();
        String authToken = userGameCommand.getAuthToken();


        switch (userGameCommand.getCommandType()) {
            case CONNECT: connect(authToken, gameID, session);
                break;
            case MAKE_MOVE:
                    MakeMoveCommand makeMoveCommand = new Gson().fromJson(message,MakeMoveCommand.class);
                    makeMove(session, authToken, gameID, makeMoveCommand.getMove());
                break;
            case LEAVE: leave(gameID, authToken, session);
                break;
            case RESIGN: resign(gameID,authToken, session);
                break;
            default:
                throw new WebSocketException(500, "invalid input");
        }


    }

//    @OnWebSocketConnect
//    public void onConnect(Session session) {}
//
//    @OnWebSocketClose
//    public void onClose(Session session) {}
//
//    @OnWebSocketError
//    public void onError (WebSocketException e) {}

    public void sendMessage(Session session, String message) {
        try {
            if (session.isOpen()) {
                session.getRemote().sendString(message);
            } else {
                System.out.println("Session is not open");
            }
        } catch(IOException e) {
//            ServerMessage sm= new ServerMessageError(ServerMessage.ServerMessageType.ERROR, e.getMessage());
//            sendMessage(session,new Gson().toJson(sm));
            System.out.println(e.getMessage());
        }
    }

    public void broadcastMessage(int gameID, String message, Session exceptThisSession) {
        Set<Session> setOfSessions = webSocketSessions.getSessionsForGame(gameID);

        for(Session session: setOfSessions) {
            if(session != exceptThisSession) {
                sendMessage(session, message);
            }
        }
    }

    public void leave(int gameID, String authToken, Session session) throws WebSocketException {
        try {
            LeaveRequest request = new LeaveRequest(gameID, authToken);
            LeaveResult result = playGameService.leave(request);

            webSocketSessions.removeSessionFromGame(gameID, session);
            ServerMessage sm= new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,result.username() + " has left the game");
            var message = new Gson().toJson(sm);
            broadcastMessage(gameID,message,session);
        } catch (WebSocketException e) {
            ErrorMessage sm= new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
            sendMessage(session,new Gson().toJson(sm));
        }
    }

    public void connect(String authToken, int gameID, Session session) {
        try {
            ConnectRequest req = new ConnectRequest(authToken, gameID);
            ConnectResult res = playGameService.connect(req);
            // add user to web socket sessions
            webSocketSessions.addSessionToGame(gameID, session);
            ServerMessage smNotify;
            ServerMessage smLoadGame;

            smNotify = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,res.username() + " joined the game");
            smLoadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,res.game());
            sendMessage(session,new Gson().toJson(smLoadGame));
            broadcastMessage(gameID,new Gson().toJson(smNotify),session);
        }catch(WebSocketException e){
            ErrorMessage sm= new ErrorMessage(ErrorMessage.ServerMessageType.ERROR, e.getMessage());
            sendMessage(session,new Gson().toJson(sm));
        }
    }

    public void resign(int gameID, String authToken, Session session) throws WebSocketException {
        try {
            ResignRequest request = new ResignRequest(gameID, authToken);
            ResignResult result = playGameService.resign(request);
            webSocketSessions.removeSessionFromGame(gameID, session);
            ServerMessage sm= new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    result.loser() + " has resigned from the game");
            var message = new Gson().toJson(sm);
            broadcastMessage(gameID, message, session);
            sendMessage(session,message);
        } catch (WebSocketException e) {
            ErrorMessage sm= new ErrorMessage(ServerMessage.ServerMessageType.ERROR,e.getMessage());
            sendMessage(session,new Gson().toJson(sm));
        }

    }

    public Object makeMove(Session session, String authToken, int gameID, ChessMove move) {
        try {
            Set<Session> sessions = webSocketSessions.getSessionsForGame(gameID);

            if (sessions.contains(session)) {
                MoveRequest moveRequest = new MoveRequest(authToken, gameID, move);
                MoveResult res = playGameService.makeMove(moveRequest);
                ServerMessage smMove = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,move.toString());
                ServerMessage smLoad = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,res.game());
                // load the new board
                broadcastMessage(gameID,new Gson().toJson(smLoad),session);
                sendMessage(session,new Gson().toJson(smLoad));
                // inform players and observers of the move
                broadcastMessage(gameID,new Gson().toJson(smMove),session);
            }
        } catch (WebSocketException e) {
            ErrorMessage sm= new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
            sendMessage(session,new Gson().toJson(sm));
        }
        return null;
    }
}
