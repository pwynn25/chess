package chess;


import java.util.Collection;

public interface ChessPieceMoveCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position);
}


