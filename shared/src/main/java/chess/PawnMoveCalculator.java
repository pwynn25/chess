package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator extends ChessPieceMoveCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position) {
        Collection<ChessMove> possiblePawnMoves = new ArrayList<>();
        if(chessBoard.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if(position.getRow() == 2) {
                if(calculateDoubleMove(chessBoard,position) != null) {
                    possiblePawnMoves.add(calculateDoubleMove(chessBoard, position));
                }
                possiblePawnMoves.addAll(calculateNormalMoves(chessBoard,position));
            }
            else if (position.getRow() < 7) {
                possiblePawnMoves.addAll(calculateNormalMoves(chessBoard,position));
            }
            else {
                possiblePawnMoves.addAll(calculatePromotionMoves(chessBoard,position));
            }
        }
        else {
            if(position.getRow() == 7) {
                if(calculateDoubleMove(chessBoard,position) != null) {
                    possiblePawnMoves.add(calculateDoubleMove(chessBoard, position));
                }
                possiblePawnMoves.addAll(calculateNormalMoves(chessBoard,position));
            }
            else if (position.getRow() > 2) {
                possiblePawnMoves.addAll(calculateNormalMoves(chessBoard,position));
            }
            else {
                possiblePawnMoves.addAll(calculatePromotionMoves(chessBoard,position));
            }

        }


        return possiblePawnMoves;
    }



    public Collection<ChessMove> calculateNormalMoves(ChessBoard board ,ChessPosition position) {
        Collection<ChessMove> normalMoves = calculateCaptureMoves(board,position);
        int directionColumn;
        int directionRow;
        ChessPosition posToCheck;
        if(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            directionColumn = 0;
            directionRow = 1;
        }
        else {
            directionColumn = 0;
            directionRow = -1;
        }
        ChessPosition potPosition = new ChessPosition(position.getRow() + directionRow, position.getColumn() + directionColumn);
        if(isInRange(potPosition.getRow()) && isEmpty(board, potPosition)) {
            ChessMove normalMove = new ChessMove(position,potPosition,null);
            normalMoves.add(normalMove);
        }
        return normalMoves;

    }
    public Collection<ChessMove> calculateCaptureMoves(ChessBoard chessBoard,ChessPosition position) {
        Collection<ChessMove> captureMoves = new ArrayList<>();
        int [] directionsColumn;
        int [] directionsRow;
        if(chessBoard.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            directionsColumn = new int[]{1,-1};
            directionsRow = new int[]{1,1};
        }
        else {
            directionsColumn = new int[]{1,-1};
            directionsRow = new int[]{-1,-1};
        }
        int potMoveColumn;
        int potMoveRow;
        for(int i = 0; i < directionsColumn.length; i ++) {
            potMoveColumn = position.getColumn() + directionsColumn[i];
            potMoveRow = position.getRow() + directionsRow[i];
            if(isInRange(potMoveColumn) && isInRange(potMoveRow)) {
                ChessPosition potPosition = new ChessPosition(potMoveRow,potMoveColumn);
                if(!isEmpty(chessBoard,potPosition)) {
                    if(isEnemyOccupied(chessBoard,position,chessBoard.getPiece(potPosition).getTeamColor())) {
                        ChessMove move = new ChessMove(position, potPosition, null);
                        captureMoves.add(move);
                    }
                }
            }
        }
        return captureMoves;
    }
    public ChessMove calculateDoubleMove(ChessBoard board,ChessPosition position) {
        ChessMove doubleMove = null;
        int directionColumn;
        int directionRow;
        ChessPosition posToCheck;
        if(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            directionColumn = 0;
            directionRow = 2;
            posToCheck = new ChessPosition(position.getRow() + 1, position.getColumn());
        }
        else {
            directionColumn = 0;
            directionRow = -2;
            posToCheck = new ChessPosition(position.getRow() - 1, position.getColumn());
        }
        ChessPosition potPosition = new ChessPosition(position.getRow() + directionRow, position.getColumn() + directionColumn);
        if(isEmpty(board, posToCheck)) {
            if(isEmpty(board, potPosition)) {
                doubleMove = new ChessMove(position,potPosition,null);
            }
        }
        return doubleMove;
    }
    public Collection <ChessMove> calculatePromotionMoves(ChessBoard chessBoard, ChessPosition position) {
        Collection <ChessMove> nonPromotedMoves = calculateNormalMoves(chessBoard,position);
        Collection <ChessMove> promotedMoves = new ArrayList<>();
        ChessPosition startPosition;
        ChessPosition endPosition;
        for (ChessMove nonPromotedMove: nonPromotedMoves) {
            startPosition = nonPromotedMove.getStartPosition();
            endPosition = nonPromotedMove.getEndPosition();
            promotedMoves.add(new ChessMove(startPosition,endPosition, ChessPiece.PieceType.QUEEN));
            promotedMoves.add(new ChessMove(startPosition,endPosition, ChessPiece.PieceType.ROOK));
            promotedMoves.add(new ChessMove(startPosition,endPosition, ChessPiece.PieceType.KNIGHT));
            promotedMoves.add(new ChessMove(startPosition,endPosition, ChessPiece.PieceType.BISHOP));
        }
        return promotedMoves;
    }
}
