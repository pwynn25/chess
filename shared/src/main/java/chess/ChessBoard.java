package chess;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    private ChessPosition whiteKing;
    private ChessPosition blackKing;

    public ChessBoard() {
        this.whiteKing = new ChessPosition(1,5);
        this.blackKing = new ChessPosition(8,5);
    }
    public ChessBoard(ChessBoard copyBoard, ChessPosition whiteKingPosition, ChessPosition blackKingPosition) {

        for(int i = 0; i < copyBoard.squares.length; i ++) {
            squares[i] = Arrays.copyOf(copyBoard.squares[i], copyBoard.squares[i].length);
        }
        this.whiteKing = whiteKingPosition;
        this.blackKing = blackKingPosition;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }
    public void removePiece(ChessPosition position) {
        squares[position.getRow()-1][position.getColumn()-1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        clearBoard();

        // Add white pieces (row 1 and 2)
        addPiece(new ChessPosition(1, 1), new ChessPiece(WHITE, ROOK));  // a1
        addPiece(new ChessPosition(1, 2), new ChessPiece(WHITE, KNIGHT)); // b1
        addPiece(new ChessPosition(1, 3), new ChessPiece(WHITE, BISHOP)); // c1
        addPiece(new ChessPosition(1, 4), new ChessPiece(WHITE, QUEEN));  // d1
        addPiece(new ChessPosition(1, 5), new ChessPiece(WHITE, KING));// e1
        addPiece(new ChessPosition(1, 6), new ChessPiece(WHITE, BISHOP)); // f1
        addPiece(new ChessPosition(1, 7), new ChessPiece(WHITE, KNIGHT)); // g1
        addPiece(new ChessPosition(1, 8), new ChessPiece(WHITE, ROOK));   // h1

        for(int i = 1; i < 9; i++) {
            addPiece(new ChessPosition(2, i), new ChessPiece(WHITE, PAWN));
        }

        // Add black pieces (row 7 and 8)
        addPiece(new ChessPosition(8, 1), new ChessPiece(BLACK, ROOK));
        addPiece(new ChessPosition(8, 2), new ChessPiece(BLACK, KNIGHT));
        addPiece(new ChessPosition(8, 3), new ChessPiece(BLACK, BISHOP));
        addPiece(new ChessPosition(8, 4), new ChessPiece(BLACK, QUEEN));
        addPiece(new ChessPosition(8, 5), new ChessPiece(BLACK, KING));
        addPiece(new ChessPosition(8, 6), new ChessPiece(BLACK, BISHOP));
        addPiece(new ChessPosition(8, 7), new ChessPiece(BLACK, KNIGHT));
        addPiece(new ChessPosition(8, 8), new ChessPiece(BLACK, ROOK));

        for(int i = 1; i < 9; i++) {
            addPiece(new ChessPosition(7, i), new ChessPiece(BLACK, PAWN));
        }
        this.whiteKing = new ChessPosition(1,5);
        this.blackKing = new ChessPosition(8,5);
    }

    /*This clears the board*/

    public void clearBoard() {
        for (int row = 1; row < 9; row++) {
            for (int column = 1; column < 9; column++) {
                squares[row-1][column-1] = null;
            }
        }
    }
    public ChessPosition getKingPosition(ChessGame.TeamColor team) {
        if(team == WHITE) {
            return whiteKing;
        }
        else{
            return blackKing;
        }
    }
    public void setKingPosition(ChessGame.TeamColor team,ChessPosition newPosition) {
        if(team == WHITE) {
            whiteKing = newPosition;
        }
        else{
            blackKing = newPosition;
        }
    }

// check if the position is on the board
    public boolean isValidPosition(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1] == null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Current board:\n");
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                sb.append(squares[i][j]).append(" | ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
