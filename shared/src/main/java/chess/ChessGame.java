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

    /* getTeamTurn() : :TeamColor
    validMoves(ChessPosition) : :Collection<ChessMove>
    makeMove(ChessMove)
    isInCheck(TeamColor) : :Boolean
    isInCheckmate(TeamColor) : :Boolean
    isInStalemate(TeamColor) : :Boolean
    setBoard(ChessBoard)
    getBoard() : :ChessBoard */
    private TeamColor teamTurn;
    private ChessBoard currentBoard;
    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.currentBoard = new ChessBoard();
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
    public static enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // this filters moves to see if they put the king in check, does this move put the king in check
        Collection <ChessMove> validMoves;

        validMoves = currentBoard.getPiece(startPosition).pieceMoves(currentBoard,startPosition);


        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection <ChessMove> validMoves = validMoves(move.getStartPosition());
        // Check the piece type
        ChessPiece.PieceType movingPieceType = currentBoard.getPiece(move.getStartPosition()).getPieceType();

        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        if(validMoves.contains(move)) {
            currentBoard.addPiece(endPosition,currentBoard.getPiece(startPosition));
            currentBoard.removePiece(startPosition);
            if(movingPieceType == KING) {
                // this sets the king's position
                currentBoard.setKingPosition(currentBoard.getPiece(endPosition).getTeamColor(),endPosition);
            }
            changeTeamTurn(this.teamTurn);
        }
        else {
            throw new InvalidMoveException("Invalid Move");
            // do we need to prompt the user again??
        }
    }
    public void changeTeamTurn(TeamColor teamTurn) {
        if (teamTurn == TeamColor.WHITE) {
            this.teamTurn = TeamColor.BLACK;
        }
        else {
            this.teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection <ChessPosition> endPositions = new ArrayList<>();
        ChessPosition kingPosition = currentBoard.getKingPosition(teamColor);
        ChessBoard copyBoard = new ChessBoard(currentBoard);

        // try all the pieceMoves
        ChessPiece.PieceType[] pieceTypes = {KING, QUEEN, ROOK, BISHOP, PAWN, KNIGHT};

        for(ChessPiece.PieceType pieceType: pieceTypes) {
            copyBoard.addPiece(kingPosition,new ChessPiece(teamColor,pieceType));
            Collection <ChessMove> moves = copyBoard.getPiece(kingPosition).pieceMoves(copyBoard,kingPosition);
            endPositions.addAll(extractEndPositions(moves));
            for(ChessPosition endPosition: endPositions) {
                if(copyBoard.getPiece(kingPosition).getPieceType()==copyBoard.getPiece(endPosition).getPieceType()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Collection<ChessPosition> extractEndPositions(Collection<ChessMove> moves) {
        Collection<ChessPosition> endPositions = new ArrayList<>();

        for(ChessMove move : moves) {
            endPositions.add(move.getEndPosition());
        }
        return endPositions;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
//   only check if checkmate is the king is already in check
    public boolean isInCheckmate(TeamColor teamColor) {
        // just loop through and see if possible moves at all of the king moves leave king still in check

        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.currentBoard = board;
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
