package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    public ChessMove chessMove;

    public MakeMoveCommand(UserGameCommand.CommandType commandType, String authToken, int gameID, ChessMove move) {
        super(commandType,authToken,gameID);
        this.chessMove = move;
    }


}
