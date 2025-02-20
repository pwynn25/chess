package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.WHITE;

public class PawnMoveCalculator extends ChessPieceMoveCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        ChessGame.TeamColor teamColor = chessBoard.getPiece(position).getTeamColor();
        int positionRow = position.getRow();
        int positionColumn = position.getColumn();

        if (IsWhite(chessBoard, position)) {
            int[] directionsColumnWhite = {0, 0, 1, -1};
            int[] directionsRowWhite = {2, 1, 1, 1};

            // if first move
            int firstMove = 1; // 1 is false
            if (positionRow == 2) {
                int columnToCheck = positionColumn + directionsColumnWhite[1];
                int rowToCheck = positionRow + directionsRowWhite[1];
                ChessPosition positionToCheck = new ChessPosition(rowToCheck, columnToCheck);
                if (isEmpty(chessBoard, positionToCheck)) {
                    firstMove = 0;// 0 is true
                }
            }
            for (int i = firstMove; i < directionsColumnWhite.length; i++) {
                int potMoveColumnWhite = positionColumn + directionsColumnWhite[i];
                int potMoveRowWhite = positionRow + directionsRowWhite[i];
                if (isInRange(potMoveColumnWhite) && isInRange(potMoveRowWhite)) {
                    ChessPosition potPosition = new ChessPosition(potMoveRowWhite, potMoveColumnWhite);

                    if (moveAllowed(chessBoard, potPosition, teamColor, directionsColumnWhite[i])) {
                        if (potPosition.getRow() == 8) {
                            Collection<ChessMove> promotionMoves = getPromotionMoves(position, potPosition);
                            possibleMoves.addAll(promotionMoves);
                        } else {
                            ChessMove move = new ChessMove(position, potPosition, null);
                            possibleMoves.add(move);
                        }
                    }
                }
            }
        } else {
            int[] directionsColumnBlack = {0, 0, 1, -1};
            int[] directionsRowBlack = {-2, -1, -1, -1};

            // if first move
            int firstMove = 1; // 1 is false
            if (positionRow == 7) {
                int columnToCheck = positionColumn + directionsColumnBlack[1];
                int rowToCheck = positionRow + directionsRowBlack[1];
                ChessPosition positionToCheck = new ChessPosition(rowToCheck, columnToCheck);
                if (isEmpty(chessBoard, positionToCheck)) {
                    firstMove = 0;// 0 is true
                }
            }
            for (int i = firstMove; i < directionsColumnBlack.length; i++) {
                int potMoveColumnWhite = positionColumn + directionsColumnBlack[i];
                int potMoveRowWhite = positionRow + directionsRowBlack[i];
                if (isInRange(potMoveColumnWhite) && isInRange(potMoveRowWhite)) {
                    ChessPosition potPosition = new ChessPosition(potMoveRowWhite, potMoveColumnWhite);

                    if (moveAllowed(chessBoard, potPosition, teamColor, directionsColumnBlack[i])) {
                        if (potPosition.getRow() == 1) {
                            Collection<ChessMove> promotionMoves = getPromotionMoves(position, potPosition);
                            possibleMoves.addAll(promotionMoves);
                        } else {
                            ChessMove move = new ChessMove(position, potPosition, null);
                            possibleMoves.add(move);
                        }
                    }
                }
            }

        }

        return possibleMoves;
    }

    public boolean IsWhite(ChessBoard chessBoard, ChessPosition position) {
        return chessBoard.getPiece(position).getTeamColor() == WHITE;
    }

    public boolean moveAllowed(ChessBoard chessBoard, ChessPosition potPosition, ChessGame.TeamColor teamColor, int potMoveColumn) {
        if (potMoveColumn != 0) {
            if (isEmpty(chessBoard, potPosition)) {
                return false;
            }
            else {
                return isEnemyOccupied(chessBoard, potPosition, teamColor);
            }
        }
        else {
            return isEmpty(chessBoard, potPosition);
        }
    }

    public Collection<ChessMove> getPromotionMoves(ChessPosition position, ChessPosition potPosition) {
        Collection<ChessMove> promotionMoves = new ArrayList<>();
        ChessMove promoteQueen = new ChessMove(position, potPosition, ChessPiece.PieceType.QUEEN);
        ChessMove promoteRook = new ChessMove(position, potPosition, ChessPiece.PieceType.ROOK);
        ChessMove promoteBishop = new ChessMove(position, potPosition, ChessPiece.PieceType.BISHOP);
        ChessMove promoteKnight = new ChessMove(position, potPosition, ChessPiece.PieceType.KNIGHT);

        promotionMoves.add(promoteQueen);
        promotionMoves.add(promoteRook);
        promotionMoves.add(promoteBishop);
        promotionMoves.add(promoteKnight);

        return promotionMoves;
    }
}
