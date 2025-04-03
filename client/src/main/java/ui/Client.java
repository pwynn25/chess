package ui;

import chess.ChessGame;

public interface Client {

    public String eval(String line);
    public String boardToString(BoardPrinter printer, ChessGame game, ChessGame.TeamColor teamColor);
}
