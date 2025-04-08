package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessPiece.PieceType.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {


    private TeamColor teamTurn;
    private ChessBoard currentBoard;
    private boolean isActive;

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.currentBoard = new ChessBoard();
        this.currentBoard.resetBoard();
        this.isActive = true;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public boolean getStatus() {
        return isActive;
    }
    public void setStatus(boolean isActive) {
        this.isActive = isActive;
    }


    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        Collection<ChessMove> validMoves;
        Collection<ChessMove> stillValidMoves = new ArrayList<>();

        validMoves = currentBoard.getPiece(startPosition).pieceMoves(currentBoard, startPosition);

        Boolean isValid;
        for (ChessMove move : validMoves) {
            isValid = isInCheckFilter(move);
            if (isValid) {
                stillValidMoves.add(move);
            }
        }
        validMoves = stillValidMoves;

        return validMoves;
    }

    // returns false when the prospective move leaves the King in Check
    public boolean isInCheckFilter(ChessMove move) {
        Boolean stillValid = true;
        ChessBoard copyBoard = new ChessBoard(currentBoard, currentBoard.getKingPosition(TeamColor.WHITE),
                currentBoard.getKingPosition(TeamColor.BLACK));


        copyBoard.addPiece(move.getEndPosition(), copyBoard.getPiece(move.getStartPosition()));
        copyBoard.removePiece(move.getStartPosition());

        // if the piece moved was a king then update the king position
        if (copyBoard.getPiece(move.getEndPosition()).getPieceType() == KING) {
            copyBoard.setKingPosition(copyBoard.getPiece(move.getEndPosition()).getTeamColor(), move.getEndPosition());
        }

        if (isInCheckHelper(copyBoard, copyBoard.getPiece(move.getEndPosition()).getTeamColor())) {
            stillValid = false;
        }

        return stillValid;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        // Check if the correct team is playing
        if (currentBoard.getPiece(move.getStartPosition()) != null) {

            //  Check if starting position contains a piece
            if (currentBoard.getPiece(move.getStartPosition()).getTeamColor() == this.teamTurn) {
                Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

                // Check the piece type to determine if it is the King
                ChessPiece.PieceType movingPieceType = currentBoard.getPiece(move.getStartPosition()).getPieceType();

                ChessPosition startPosition = move.getStartPosition();
                ChessPosition endPosition = move.getEndPosition();

                // is the move valid??
                if (validMoves.contains(move)) {

                    // check to see if the move is promotion move
                    if (move.getPromotionPiece() != null) {
                        currentBoard.addPiece(endPosition, new ChessPiece(teamTurn, move.getPromotionPiece()));
                    } else {
                        currentBoard.addPiece(endPosition, currentBoard.getPiece(startPosition));
                    }
                    // remove the piece that was there
                    currentBoard.removePiece(startPosition);

                    // update king position if piece moved was a King
                    if (movingPieceType == KING) {
                        // this sets the king's position
                        currentBoard.setKingPosition(currentBoard.getPiece(endPosition).getTeamColor(), endPosition);
                    }
                    if (isInStalemate(this.teamTurn)) {
                        throw new InvalidMoveException("Your methods worked!!!");
                    }
                    changeTeamTurn(this.teamTurn);
                } else {
                    throw new InvalidMoveException("Invalid Move");
                    // do we need to prompt the user again??
                }
            } else {
                throw new InvalidMoveException("Invalid Move: no piece there");
            }
        } else {
            throw new InvalidMoveException("Invalid Move: not your turn");
        }
    }


    // helper function
    public void changeTeamTurn(TeamColor teamTurn) {
        if (teamTurn == TeamColor.WHITE) {
            this.teamTurn = TeamColor.BLACK;
        } else {
            this.teamTurn = TeamColor.WHITE;
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    // this function checks the current board to see if its in check
    public boolean isInCheck(TeamColor teamColor) {
        ChessBoard copyBoard = new ChessBoard(currentBoard, currentBoard.getKingPosition(TeamColor.WHITE),
                currentBoard.getKingPosition(TeamColor.BLACK));
        return isInCheckHelper(copyBoard, teamColor);
    }

    public Collection<ChessPosition> extractEndPositions(Collection<ChessMove> moves) {
        Collection<ChessPosition> endPositions = new ArrayList<>();

        for (ChessMove move : moves) {
            endPositions.add(move.getEndPosition());
        }
        return endPositions;
    }


    public boolean isInCheckHelper(ChessBoard board, TeamColor teamColor) {
        ChessPosition kingPosition = board.getKingPosition(teamColor);

        // try all the pieceMoves
        ChessPiece.PieceType[] pieceTypes = {KING, QUEEN, ROOK, BISHOP, PAWN, KNIGHT};

        for (ChessPiece.PieceType pieceType : pieceTypes) {
            Collection<ChessPosition> endPositions = new ArrayList<>();
            board.addPiece(kingPosition, new ChessPiece(teamColor, pieceType));
            Collection<ChessMove> moves = board.getPiece(kingPosition).pieceMoves(board, kingPosition);
            endPositions.addAll(extractEndPositions(moves));
            ChessPiece.PieceType pieceInCheck = board.getPiece(kingPosition).getPieceType();
            // check which moves involve a piece of the same type
            // the king is in check if the pieces are of the same type
            for (ChessPosition endPosition : endPositions) {

                ChessPiece pieceCausingCheck = board.getPiece(endPosition);
                if (pieceCausingCheck != null) {
                    if (pieceInCheck == pieceCausingCheck.getPieceType()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
//   only check if checkmate is the king is already in check
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return isInStalemateHelper(teamColor);
        } else {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        } else {
            return isInStalemateHelper(teamColor);
        }
    }

    //  returns true if there are no valid moves that take the king out of check
    public boolean isInStalemateHelper(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                if (isSameColor(pos,teamColor)) {
                        Collection<ChessMove> validMoves = validMoves(pos);
                        if (!validMoves.isEmpty()) {
                            return false;
                        }
                }
            }
        }
        return true;
    }
    public boolean isSameColor(ChessPosition pos, TeamColor teamColor) {
        if (currentBoard.getPiece(pos) != null) {
            if (currentBoard.getPiece(pos).getTeamColor() == teamColor) {
                return true;

            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.currentBoard = board;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                if (currentBoard.getPiece(pos) != null) {
                    if (currentBoard.getPiece(pos).getPieceType() == ChessPiece.PieceType.KING) {
                        setKing(pos);
                    }
                }
            }
        }
    }

    public void setKing (ChessPosition pos){
        if (currentBoard.getPiece(pos).getTeamColor() == TeamColor.WHITE) {
            currentBoard.setKingPosition(TeamColor.WHITE, pos);
        } else {
            currentBoard.setKingPosition(TeamColor.BLACK, pos);
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */

    public ChessBoard getBoard() {

        return currentBoard;
    }


}
