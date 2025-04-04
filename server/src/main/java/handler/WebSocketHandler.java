package handler;

import com.google.gson.Gson;
import dataaccess.GameDAO;
import exception.WebSocketException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import request.ConnectRequest;
import request.LeaveRequest;
import request.ResignRequest;
import result.ConnectResult;
import result.LeaveResult;
import result.ResignResult;
import service.PlayGameService;
import websocket.commands.UserGameCommand;
import websocket.messages.Notifier;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessageError;

import java.util.Objects;

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
//            case MAKE_MOVE: makeMove(session, move); // you need to know what session you
            case LEAVE: leave(gameID, authToken, session);
            case RESIGN: resign(gameID,authToken, session);

            break;
            default:
                throw new WebSocketException(500, "invalid input");
        }

    }

    @OnWebSocketConnect
    public void onConnect(Session session) {}

    @OnWebSocketClose
    public void onClose(Session session) {}

    @OnWebSocketError
    public void onError (WebSocketException e) {


    }

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
    public Object leave(int gameID, String authToken, Session session) throws WebSocketException {
        try {
            LeaveRequest request = new LeaveRequest(gameID, authToken);
            LeaveResult result = playGameService.leave(request);

            webSocketSessions.removeSessionFromGame(gameID, session);

//            broadcastMessage(gameID, result.message(), session);
            ServerMessage sm= new Notifier(ServerMessage.ServerMessageType.NOTIFICATION);
            ((Notifier) sm).setMessage(result.username() + " has left the game");
            return sm;
        } catch (WebSocketException e) {
            ServerMessage sm= new ServerMessageError(ServerMessage.ServerMessageType.ERROR);
            ((ServerMessageError) sm).setMessage(e.getMessage());
            return sm;
        }
    }

    public Object connect(String authToken, int gameID, Session session) {
        try {// verify the auth token
            ConnectRequest req = new ConnectRequest(authToken, gameID);
            ConnectResult res = playGameService.connect(req);
            // add user to web socket sessions
            webSocketSessions.addSessionToGame(gameID, session);
            ServerMessage sm = new Notifier(ServerMessage.ServerMessageType.NOTIFICATION);
            if (isObserver(res.username(), res.game())) {
                ((Notifier) sm).setMessage(res.username() + " is observing the game");
                return sm;
            }
            else {
                ((Notifier) sm).setMessage(res.username() + " joined the game");
                return sm;
            }
        }catch(WebSocketException e){
            ServerMessage sm = new ServerMessageError(ServerMessage.ServerMessageType.ERROR);
            ((ServerMessageError) sm).setMessage(e.getMessage());
            return sm;
        }
    }
    private boolean isObserver(String username, GameData game) {
        return !Objects.equals(username, game.getWhiteUsername()) && !Objects.equals(username, game.getBlackUsername());
    }

    public Object resign(int gameID, String authToken, Session session) throws WebSocketException {
        try {
            ResignRequest request = new ResignRequest(gameID, authToken);
            ResignResult result = playGameService.resign(request);

            webSocketSessions.removeSessionFromGame(gameID, session);

            ServerMessage sm= new Notifier(ServerMessage.ServerMessageType.NOTIFICATION);
            ((Notifier) sm).setMessage("Someone has resigned from the game");
//            broadcastMessage(gameID, result.message(), session);
            return sm;
        } catch (WebSocketException e) {
            ServerMessage sm= new ServerMessageError(ServerMessage.ServerMessageType.ERROR);
            ((ServerMessageError) sm).setMessage(e.getMessage());
            return sm;
        }

    }

    public Object makeMove(int gameID, String authToken) {

        return null;
    }




}
