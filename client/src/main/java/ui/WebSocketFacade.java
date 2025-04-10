package ui;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.WebSocketException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class WebSocketFacade extends Endpoint implements MessageHandler.Whole<String> {
    private Session session;
    private GameHandler gameHandler;

    public WebSocketFacade(String url, InGameClient inGameClient) throws WebSocketException {
        gameHandler = inGameClient;
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(this);

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new WebSocketException(500, ex.getMessage());
        }
    }

    @Override
    public void onMessage(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

        if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
            gameHandler.printMessage(notificationMessage.getMessage());
        }
        else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
            gameHandler.updateGame(loadGameMessage.getGame());
        }
        else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
            gameHandler.printMessage(errorMessage.getMessage());
        }

    }

    private <T> void sendMessage(T userGameCommand) throws WebSocketException{
        String message = new Gson().toJson(userGameCommand);
        try {
            System.out.println("Socket open? " + (session != null && session.isOpen()));
            assert this.session != null;
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            throw new WebSocketException(500, e.getMessage());
        }
    };
    public void connect(int gameID, String authToken) throws WebSocketException{
        UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT,authToken,gameID);
        sendMessage(userGameCommand);
    }
    public void makeMove(int gameID, String authToken, ChessMove move) throws WebSocketException{
        MakeMoveCommand makeMoveCommand = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE
                ,authToken,gameID,move);
        sendMessage(makeMoveCommand);
    }
    public void leave(int gameID, String authToken) throws WebSocketException{
        UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE,authToken,gameID);
        sendMessage(userGameCommand);
    }
    public void resign(int gameID, String authToken) throws WebSocketException{
        UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN,authToken,gameID);
        sendMessage(userGameCommand);
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
//    @Override
//    public void onClose(Session session, EndpointConfig endpointConfig) {
//
//    }
//    @Override
//    public void onError(Session session, EndpointConfig endpointConfig) {
//
//    }

}
