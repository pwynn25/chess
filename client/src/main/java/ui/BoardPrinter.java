package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class BoardPrinter {
    private static final int[] WHITE= {8,1,1,8};
    private static final int[] BLACK={1,8,8,1};
    private static final String[] LETTERS = {"a","b","c","d","e","f","g","h"};
    private static final int[] NUMBERS = {1,2,3,4,5,6,7,8};
    private static Collection<ChessPosition> positionsToHighlight = new ArrayList<>();
    private static ChessPosition startPosition;

    public BoardPrinter() {

    }
    public BoardPrinter(Collection<ChessPosition> positionsToHighlight, ChessPosition startPosition) {
        BoardPrinter.positionsToHighlight = positionsToHighlight;
        BoardPrinter.startPosition = startPosition;
    }


    public String printTopAndBottomEdge(boolean isWhite) {
        StringBuilder sb = new StringBuilder(SET_TEXT_COLOR_WHITE);
        sb.append("   ");
        if(isWhite) {
            for(String letter: LETTERS) {
                sb.append(" ").append(letter).append(" ");
            }
        }
        else {
            for(int i = LETTERS.length-1; i >= 0; i--) {
                sb.append(" ").append(LETTERS[i]).append(" ");
            }
        }
        sb.append("   ").append("\n");
        return sb.toString();
    }



    public String printBoard(ChessBoard board, ChessGame.TeamColor teamColor) {
        int[] loopParams;
        int maxColumn = 8;
        int minColumn = 1;
        int minRow = 1;
        int maxRow = 8;
        StringBuilder sb = new StringBuilder(SET_TEXT_COLOR_BLACK);
        String bgColor;
        sb.append(printTopAndBottomEdge(teamColor == ChessGame.TeamColor.WHITE));
        if(teamColor == ChessGame.TeamColor.WHITE) {
            int row = 8;
            for (int i = maxRow; i >= minRow; i--) {
                sb.append(addNumber(row));
                for (int j = minColumn; j <= maxColumn; j++) {
                        if(positionsToHighlight.contains(new ChessPosition(i,j))) {
                            System.out.println(new ChessPosition(i,j));
                            bgColor = SET_BG_COLOR_RED;
                            sb.append(drawSpot(i,j,bgColor,board));
                        }
                        else if (Objects.equals(startPosition, new ChessPosition(i, j))) {
                            bgColor = SET_BG_COLOR_YELLOW;
                            sb.append(drawSpot(i,j,bgColor,board));
                        }
                        else {
                            bgColor = setSquareColor(i, j);
                            sb.append(drawSpot(i, j, bgColor, board));
                        }
                }
                sb.append(addNumber(row));
                sb.append("\n");
                row--;
            }
        }
        else {
            int row = 1;
            loopParams = BLACK;
            for (int i = loopParams[0]; i <= loopParams[1]; i++) {
                sb.append(addNumber(row));
                for (int j = loopParams[2]; j >= loopParams[3]; j--) {
                    bgColor = setSquareColor(i,j);
                    sb.append(drawSpot(i, j, bgColor, board));
                }
                sb.append(addNumber(row));
                sb.append("\n");
                row++;
            }
        }
        sb.append(printTopAndBottomEdge(teamColor == ChessGame.TeamColor.WHITE));
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

    private String setSquareColor(int i,int j) {
        String bgColor;
        if ((i + j) % 2 == 0) {
            // Light square
            bgColor = SET_BG_COLOR_BLUE; // You can adjust this color for light squares
        } else {
            // Dark square
            bgColor = SET_BG_COLOR_WHITE; // You can set this to a color of your choice for dark squares
        }
        return bgColor;
    }
    private String addNumber(int row) {
        StringBuilder sb = new StringBuilder(SET_TEXT_COLOR_WHITE);
        sb.append(" ").append(NUMBERS[row-1]).append(" ");
        sb.append(RESET_TEXT_COLOR);
        return sb.toString();
    }

}
