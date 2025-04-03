package handler;

import com.google.gson.Gson;
import exception.ExceptionResponse;
import exception.WebSocketException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import request.LeaveRequest;
import request.LoginRequest;
import request.ResignRequest;
import result.LeaveResult;
import result.LoginResult;
import result.ResignResult;
import service.PlayGameService;
import spark.Request;
import spark.Response;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebSocketHandler {
    PlayGameService playGameService;
    WebSocketSessions webSocketSessions;

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
            case CONNECT: connect();
            case MAKE_MOVE: makeMove();
            case LEAVE: leave(gameID, authToken);
            case RESIGN: resign();

            break;
            default:
                throw new WebSocketException("invalid input");
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

    }

    public void broadcastMessage(int gameID, String message, Session exceptThisSession) {

    }
        // call the appropriate function , these methods call different services
        // connect(message)
        // makeMove(message)
        //leaveGame(message)

    public Object leave(int gameID, String authToken) throws WebSocketException {
        String message;
        LeaveRequest request = new LeaveRequest(gameID, authToken);
        LeaveResult result = playGameService.leave(request);

        webSocketSessions.removeSessionFromGame(gameID,session);

        broadcastMessage(gameID, result.message(), session);
        return new Gson().toJson(result);
    }

    public Object connect() throws ExceptionResponse {

    }

    public Object resign(int gameID, String authToken) throws ExceptionResponse {
        String message;
        ResignRequest request = new ResignRequest(gameID,authToken);
        ResignResult result = playGameService.resign(request);

        webSocketSessions.removeSessionFromGame(gameID,session);

        broadcastMessage(gameID, result.message(), session);
        return new Gson().toJson(result);
    }


        //resignGame(message)



}
