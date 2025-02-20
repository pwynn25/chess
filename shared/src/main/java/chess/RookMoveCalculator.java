package chess;


import java.util.Collection;

public class RookMoveCalculator extends ChessPieceMoveCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position) {
        Collection<ChessMove> possibleMoves;
        possibleMoves = calculateOrthogonalMoves(chessBoard, position);


        return possibleMoves;
    }
}
