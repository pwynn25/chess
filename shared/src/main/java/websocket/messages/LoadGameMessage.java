package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage{
    private GameData game;
    private ServerMessageType serverMessageType;
    public LoadGameMessage(ServerMessageType serverMessageType) {
        this.serverMessageType = serverMessageType;
    }

    public GameData getGame() {
        return game;
    }

    public void setGame(GameData game) {
        this.game = game;
    }
}
