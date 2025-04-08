package websocket.messages;

import chess.ChessGame;
import model.GameData;

public class LoadGameMessage extends ServerMessage{
    private GameData game;
    private ServerMessageType serverMessageType;
    public LoadGameMessage(ServerMessageType serverMessageType, GameData gameData) {
        super(serverMessageType);
        this.game = gameData;

    }

    public GameData getGame() {
        return game;
    }

    public void setGame(GameData game) {
        this.game = game;
    }
}
