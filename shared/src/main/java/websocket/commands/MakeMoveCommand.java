package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    public ChessMove move;

    public MakeMoveCommand(UserGameCommand.CommandType commandType, String authToken, int gameID, ChessMove move) {
        super(commandType,authToken,gameID);
        this.move = move;
    }
    @Override
    public ChessMove getMove() {
        return move;
    }

    public void setMove(ChessMove move) {
        this.move = move;
    }
}
