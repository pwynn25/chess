package chess;


import java.util.ArrayList;
import java.util.Collection;

public abstract class ChessPieceMoveCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position) {
        return null;
    }

    public static boolean isEmpty(ChessBoard board, ChessPosition position) {
        return board.getPiece(position) == null;
    }

    public static boolean isEnemyOccupied(ChessBoard board, ChessPosition potMove, ChessGame.TeamColor movingTeamColor) {
        return board.getPiece(potMove).getTeamColor() != movingTeamColor;
    }

    public static boolean isInRange(int value) {
        return value > 0 && value <= 8;
    }

    public static Collection<ChessMove> calculateDiagonalMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleDiagonalMoves = new ArrayList<>();
        ChessGame.TeamColor teamColor = board.getPiece(position).getTeamColor();
        int[] directionsColumn = {1, 1, -1, -1};
        int[] directionsRow = {1, -1, -1, 1};

        int positionRow = position.getRow();
        int positionColumn = position.getColumn();

        for (int i = 0; i < directionsColumn.length; i++) {
            int potMoveColumn = positionColumn + directionsColumn[i];
            int potMoveRow = positionRow + directionsRow[i];
            while (isInRange(potMoveColumn) && isInRange(potMoveRow)) {

                ChessPosition potPosition = new ChessPosition(potMoveRow, potMoveColumn);
                if (isEmpty(board, potPosition)) {
                    ChessMove move = new ChessMove(position, potPosition, null);
                    possibleDiagonalMoves.add(move);
                } else if (isEnemyOccupied(board, potPosition, teamColor)) {
                    ChessMove move = new ChessMove(position, potPosition, null);
                    possibleDiagonalMoves.add(move);
                    break;
                } else {
                    break;
                }
                potMoveColumn += directionsColumn[i];
                potMoveRow += directionsRow[i];
            }
        }
        return possibleDiagonalMoves;
    }

    public static Collection<ChessMove> calculateOrthogonalMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibleDiagonalMoves = new ArrayList<>();
        ChessGame.TeamColor teamColor = board.getPiece(position).getTeamColor();
        int[] directionsColumn = {1, 0, -1, 0};
        int[] directionsRow = {0, -1, 0, 1};

        int positionRow = position.getRow();
        int positionColumn = position.getColumn();

        for (int i = 0; i < directionsColumn.length; i++) {
            int potMoveColumn = positionColumn + directionsColumn[i];
            int potMoveRow = positionRow + directionsRow[i];
            while (isInRange(potMoveColumn) && isInRange(potMoveRow)) {

                ChessPosition potPosition = new ChessPosition(potMoveRow, potMoveColumn);
                if (isEmpty(board, potPosition)) {
                    ChessMove move = new ChessMove(position, potPosition, null);
                    possibleDiagonalMoves.add(move);
                } else if (isEnemyOccupied(board, potPosition, teamColor)) {
                    ChessMove move = new ChessMove(position, potPosition, null);
                    possibleDiagonalMoves.add(move);
                    break;
                } else {
                    break;
                }
                potMoveColumn += directionsColumn[i];
                potMoveRow += directionsRow[i];
            }
        }
        return possibleDiagonalMoves;
    }
}


