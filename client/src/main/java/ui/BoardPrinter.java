package ui;

import chess.ChessBoard;
import chess.ChessPosition;

import static ui.EscapeSequences.SET_BG_COLOR_LIGHT_GREY;

public class BoardPrinter {
    public String printBoardWhiteDown(ChessBoard board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 8; i >= 1; i--) {
            for (int j = 1; j < 9; j++) {
                sb.append(SET_BG_COLOR_LIGHT_GREY).append(" ");
                ChessPosition pos = new ChessPosition(i,j);
                if(board.getPiece(pos) == null) {
                    sb.append(" ");
                }
                else {
                    sb.append(board.getPiece(new ChessPosition(i, j)));
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String printBoardBlackDown(ChessBoard board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 8; i++) {
            for (int j = 8; j > 0; j--) {
                sb.append(" ");
                ChessPosition pos = new ChessPosition(i,j);
                if(board.getPiece(pos) == null) {
                    sb.append(" ");
                }
                else {
                    sb.append(board.getPiece(new ChessPosition(i, j)));
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
