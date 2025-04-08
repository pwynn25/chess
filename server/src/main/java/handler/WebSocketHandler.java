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
import result.ResignResult;
import service.PlayGameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notifier;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessageError;

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

        if (userGameCommand.getCommandType()  == UserGameCommand.CommandType.MAKE_MOVE) {
            userGameCommand = new Gson().fromJson(message,MakeMoveCommand.class);
        }

        int gameID = userGameCommand.getGameID();
        String authToken = userGameCommand.getAuthToken();


        switch (userGameCommand.getCommandType()) {
            case CONNECT: connect(authToken, gameID, session);
            case MAKE_MOVE: makeMove(session,authToken,gameID,userGameCommand.getChessMove()); // check this method with a TA!
            case LEAVE: leave(gameID, authToken, session);
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
        // a web socket command is supposed to go here

    }

    public void broadcastMessage(int gameID, String message, Session exceptThisSession) {
        // a web socket command is supposed to go here
//        sendMessage(session, message);
    }
        // call the appropriate function , these methods call different services
        // connect(message)
        // makeMove(message)
        //leaveGame(message)

    // these functions are going to call broadcast and stuff
    public void leave(int gameID, String authToken, Session session) throws WebSocketException {
        try {
            LeaveRequest request = new LeaveRequest(gameID, authToken);
            LeaveResult result = playGameService.leave(request);

            webSocketSessions.removeSessionFromGame(gameID, session);
            ServerMessage sm= new Notifier(ServerMessage.ServerMessageType.NOTIFICATION,result.username() + " has left the game");
            var message = new Gson().toJson(sm);
            broadcastMessage(gameID,message,session);
        } catch (WebSocketException e) {
            ServerMessage sm= new ServerMessageError(ServerMessage.ServerMessageType.ERROR);
            ((ServerMessageError) sm).setMessage(e.getMessage());
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
            smNotify = new Notifier(ServerMessage.ServerMessageType.NOTIFICATION,res.username() + " is observing the game");
            smLoadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,res.game());
            sendMessage(session,new Gson().toJson(smLoadGame));
            broadcastMessage(gameID,new Gson().toJson(smNotify),session);
        }catch(WebSocketException e){
            ServerMessage sm= new ServerMessageError(ServerMessage.ServerMessageType.ERROR);
            ((ServerMessageError) sm).setMessage(e.getMessage());
            sendMessage(session,new Gson().toJson(sm));
        }
    }
    private boolean isObserver(String username, GameData game) {
        return !Objects.equals(username, game.getWhiteUsername()) && !Objects.equals(username, game.getBlackUsername());
    }

    public void resign(int gameID, String authToken, Session session) throws WebSocketException {
        try {
            ResignRequest request = new ResignRequest(gameID, authToken);
            ResignResult result = playGameService.resign(request);

            webSocketSessions.removeSessionFromGame(gameID, session);

            ServerMessage sm= new Notifier(ServerMessage.ServerMessageType.NOTIFICATION,
                    result.winner() + "has won the game because the other player resigned");
//          broadcastMessage(gameID, result.message(), session);
            return sm;
        } catch (WebSocketException e) {
            ServerMessage sm= new ServerMessageError(ServerMessage.ServerMessageType.ERROR);
            ((ServerMessageError) sm).setMessage(e.getMessage());
            sendMessage(session,new Gson().toJson(sm));
        }

    }

    public Object makeMove(Session session, String authToken, int gameID, ChessMove move) {
        try {
            Set<Session> sessions = webSocketSessions.getSessionsForGame(gameID);

            if (sessions.contains(session)) {
                MoveRequest moveRequest = new MoveRequest(authToken, gameID, move);
                playGameService.makeMove(moveRequest);


            }
        } catch (WebSocketException e) {
            ServerMessage sm= new ServerMessageError(ServerMessage.ServerMessageType.ERROR);
            ((ServerMessageError) sm).setMessage(e.getMessage());
            sendMessage(session,new Gson().toJson(sm));
        }
        return null;
    }




}
