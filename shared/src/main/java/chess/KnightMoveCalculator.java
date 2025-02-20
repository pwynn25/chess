package chess;


import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator extends ChessPieceMoveCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position) {
        return calcKingAndKnightMoves(chessBoard,position, ChessPiece.PieceType.KNIGHT);
    }
}
