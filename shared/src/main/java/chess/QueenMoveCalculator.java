package chess;


import java.util.Collection;

public class QueenMoveCalculator extends ChessPieceMoveCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position) {
        Collection<ChessMove> possibleMoves;
        Collection<ChessMove> additionalMoves;
        possibleMoves = calculateOrthogonalMoves(chessBoard, position);
        additionalMoves = calculateDiagonalMoves(chessBoard, position);

        possibleMoves.addAll(additionalMoves);

        return possibleMoves;
    }
}
