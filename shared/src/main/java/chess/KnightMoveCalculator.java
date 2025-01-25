package chess;


import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator extends ChessPieceMoveCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position){
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        ChessGame.TeamColor teamColor = chessBoard.getPiece(position).getTeamColor();
        int[] directionsColumn = {-1, 1, 2, 2,-2,-2, -1, 1};
        int[] directionsRow = {2, 2, -1, 1, -1, 1, -2, -2};


        int positionRow = position.getRow();
        int positionColumn = position.getColumn();


        for (int i = 0; i < directionsColumn.length; i++) {
            int potMoveColumn = positionColumn + directionsColumn[i];
            int potMoveRow = positionRow + directionsRow[i];
            if (isInRange(potMoveColumn) && isInRange(potMoveRow)) {

                ChessPosition potPosition = new ChessPosition(potMoveRow, potMoveColumn);
                if (isEmpty(chessBoard,potPosition)) {
                    ChessMove move = new ChessMove(position, potPosition, null);
                    possibleMoves.add(move);
                }
                else if (isEnemyOccupied(chessBoard,potPosition,teamColor)) {
                    ChessMove move = new ChessMove(position, potPosition, null);
                    possibleMoves.add(move);
                }
            }
        }


        return possibleMoves;
    }
}
