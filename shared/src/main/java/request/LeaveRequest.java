package request;

import chess.ChessGame;

public record LeaveRequest (int gameID, String authToken){
}
