package chess;

import java.util.Collection;

public class BishopMoveCalculator extends ChessPieceMoveCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position) {
        Collection<ChessMove> possibleMoves;
        possibleMoves = calculateDiagonalMoves(chessBoard, position);

        return possibleMoves;
    }
}
