package chess;

import java.util.Collection;
import java.util.Objects;

import static chess.ChessGame.TeamColor.WHITE;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor teamColor;
    private PieceType pieceType;
    private Collection<ChessMove> moves;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.teamColor = pieceColor;
        // forgot to commit
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPieceMoveCalculator moves = switch (getPieceType()) {
            case QUEEN -> new QueenMoveCalculator();
            case KNIGHT -> new KnightMoveCalculator();
            case ROOK -> new RookMoveCalculator();
            case PAWN -> new PawnMoveCalculator();
            case KING -> new KingMoveCalculator();
            case BISHOP -> new BishopMoveCalculator();
        };
        return moves.pieceMoves(board, position);
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && pieceType == that.pieceType && Objects.equals(moves, that.moves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, pieceType, moves);
    }

    @Override
    public String toString() {
        if (teamColor == ChessGame.TeamColor.BLACK) {
            if (pieceType == ChessPiece.PieceType.KNIGHT) {
                return "n";
            }
            if (pieceType == ChessPiece.PieceType.ROOK) {
                return "r";
            }
            if (pieceType == ChessPiece.PieceType.PAWN) {
                return "p";
            }
            if (pieceType == ChessPiece.PieceType.KING) {
                return "k";
            }
            if (pieceType == ChessPiece.PieceType.BISHOP) {
                return "b";
            }
            if (pieceType == ChessPiece.PieceType.QUEEN) {
                return "q";
            }
        }
        else {
            if (pieceType == ChessPiece.PieceType.KNIGHT) {
                return "N";
            }
            if (pieceType == ChessPiece.PieceType.ROOK) {
                return "R";
            }
            if (pieceType == ChessPiece.PieceType.PAWN) {
                return "P";
            }
            if (pieceType == ChessPiece.PieceType.KING) {
                return "K";
            }
            if (pieceType == ChessPiece.PieceType.BISHOP) {
                return "B";
            }
            if (pieceType == ChessPiece.PieceType.QUEEN) {
                return "Q";
            }
            else {
                return "null";
            }
        }
        return "";
    }
}
