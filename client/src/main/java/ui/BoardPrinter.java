package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardPrinter {
    private final int[] WHITE= {8,0,1,9};
    private final int[] BLACK={1,9,8,0};
    public String printBoard(ChessBoard board, ChessGame.TeamColor teamColor) {
        int[] loopParams;
        StringBuilder sb = new StringBuilder(SET_TEXT_COLOR_BLACK);
        String bgColor;
        if(teamColor == ChessGame.TeamColor.WHITE) {
            loopParams = WHITE;
            for (int i = loopParams[0]; i > loopParams[1]; i--) {
                for (int j = loopParams[2]; j < loopParams[3]; j++) {
                    if ((i + j) % 2 == 0) {
                        // Light square
                        bgColor = SET_BG_COLOR_MAGENTA; // You can adjust this color for light squares
                    } else {
                        // Dark square
                        bgColor = SET_BG_COLOR_LIGHT_GREY; // You can set this to a color of your choice for dark squares
                    }
                    sb.append(drawSpot(i,j,bgColor,board));
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        else {
            loopParams = BLACK;
            for (int i = loopParams[0]; i < loopParams[1]; i++) {
                for (int j = loopParams[2]; j > loopParams[3]; j--) {
                    if ((i + j) % 2 == 0) {
                    // Light square
                        bgColor = SET_BG_COLOR_MAGENTA; // You can adjust this color for light squares
                    } else {
                    // Dark square
                        bgColor = SET_BG_COLOR_LIGHT_GREY; // You can set this to a color of your choice for dark squares
                    }
                    sb.append(drawSpot(i, j, bgColor, board));
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String drawSpot(int i, int j,String bgColor, ChessBoard board) {
        StringBuilder sb = new StringBuilder(SET_TEXT_COLOR_BLACK);
        sb.append(bgColor);
        ChessPosition pos = new ChessPosition(i,j);
        if(board.getPiece(pos) == null) {
            sb.append("   ");
        }
        else {
            sb.append(" ").append(board.getPiece(new ChessPosition(i, j))).append(" ");
        }
        sb.append(RESET_BG_COLOR);
        return sb.toString();
    }

}
