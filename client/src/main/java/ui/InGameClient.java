package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.WebSocketException;
import model.GameData;
import websocket.commands.UserGameCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static chess.ChessPiece.PieceType.*;


public class InGameClient implements Client,GameHandler{
    private ServerFacade server;
    private Repl repl;
    private ChessGame game;
    private ChessGame.TeamColor teamColor;
    private WebSocketFacade wsFacade;
    private int gameID;
    private String authToken;

    public InGameClient(Repl repl) {
        this.server = repl.server;
        this.repl = repl;
        this.teamColor = repl.teamColor;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void establishConnection() {
        try {
            this.wsFacade = new WebSocketFacade(repl.serverURL, this);
            this.wsFacade.connect(this.gameID,this.authToken);
        } catch (WebSocketException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());

        }
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void updateGame(GameData game) {
        this.game = game.getGame();
        System.out.println(boardToString(new BoardPrinter(),this.game,this.teamColor));
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(ChessGame.TeamColor teamColor) {
        this.teamColor = teamColor;
    }

    public String eval(String input){
        try {
            var tokens = input.split(" ");
            var cmd = tokens[0];
            var params = Arrays.copyOfRange(tokens,1,tokens.length);
            return switch (cmd) {
                case "leave" -> leave();
                case "resign" -> resign();
                case "redraw" -> redraw();
                case "highlight" -> highlight(params);
                case "move" -> makeMove(params);
                default -> help();
            };
        }
        catch(InputError e) {
            return e.getErrorMessage() + "\n";
        }
    }


    public String help() {
        return """
                Options:
                    Redraw the chess board: "redraw"
                    leave the game: "leave"
                    Make a move: "move" <START> <END>
                    Highlight moves: "hl" <START>
                    Resign from the game: "resign"
                    help: "help"
                """;

    }

    public String makeMove(String...params) throws InputError{
        ChessMove move;
        ChessPosition startPosition;
        ChessPosition endPosition;
        try {
            startPosition =  isValidSquareOnBoard(params[0]);
            endPosition = isValidSquareOnBoard(params[1]);
            if(params.length == 3) {
                ChessPiece.PieceType pPType = checkPromotionPiece(params[2]);
                move = new ChessMove(startPosition,endPosition,pPType);
            }
            else if(params.length == 2) {
                move = new ChessMove(startPosition,endPosition,null);
            }
            else {
                throw new InputError("Invalid Input: Make a move: \"move\" <START> <END>");
            }
        }catch(InputError e) {
            throw new InputError("Invalid Input: Make a move: \"move\" <START> <END>");
        }
        try{
            wsFacade.makeMove(this.gameID,this.authToken,move);
        } catch(WebSocketException e) {
            System.out.println("unable to maintain websocket connection");
        }
            return  "\n";
    }

    public String resign() {
        try {
            wsFacade.resign(this.gameID, this.authToken);
            return "You have resigned from the game!";
        } catch (WebSocketException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String highlight(String...params) throws InputError{
        Collection<ChessPosition> positions = new ArrayList<>();
        if(params.length != 1) {
            throw new InputError("Highlight moves: \"hl\" <START>");
        }
        try {
            ChessPosition posToHl = isValidSquareOnBoard(params[0]);
            ChessPiece piece = game.getBoard().getPiece(posToHl);
            if (piece == null) {
                throw new InputError("Input Error: invalid position");
            }
            else {
                Collection<ChessMove> moves = this.game.validMoves(posToHl);
                // you need to check if the moves he is asking for are his
                for (ChessMove move : moves) {
                    positions.add(move.getEndPosition());
                }
            }
            return boardToString(new BoardPrinter(positions,posToHl), this.game, repl.teamColor);
        }catch(InputError e) {
            throw new InputError("Highlight moves: \"hl\" <START>");
        }
    }
    public String leave() {
        repl.setUserStatus(Repl.UserStatus.LOGGED_IN);
        try {
            wsFacade.leave(gameID, authToken);
            return "You have left the game!";
        } catch(WebSocketException e) {
            return "Error: " + e.getMessage();
        }
    }
    public String redraw() {
        return boardToString(new BoardPrinter(), this.game, teamColor);
    }




    // Helper Functions

    public ChessPosition isValidSquareOnBoard(String param) throws InputError{
        int validRow;
        int validColumn;
        if(2 == param.length()) {
            char[] chars = param.toCharArray();
            char column = chars[0];
            char row = chars[1];
            if(Character.isDigit(row)) {
                validRow = row - '0';
                if(validRow < 1 || validRow > 8) {
                    throw new InputError("invalid input");
                }
            }
            else {
                throw new InputError("invalid input");
            }
            if (Character.isAlphabetic(column)) {
                char lowerCaseColumn = Character.toLowerCase(column);
                validColumn = switch (lowerCaseColumn) {
                    case 'a' -> 1;
                    case 'b' -> 2;
                    case 'c' -> 3;
                    case 'd' -> 4;
                    case 'e' -> 5;
                    case 'f' -> 6;
                    case 'g' -> 7;
                    case 'h' -> 8;
                    // Valid column, do nothing
                    default -> throw new InputError("invalid input");
                };
            }
            else {
                throw new InputError("invalid input");
            }
            return new ChessPosition(validRow,validColumn);
        }
        else {
            throw new InputError("invalid input");
        }
    }
    public String boardToString(BoardPrinter printer, ChessGame game, ChessGame.TeamColor teamColor) {
        return printer.printBoard(game.getBoard(),teamColor);
    }
    public ChessPiece.PieceType checkPromotionPiece(String thirdParam) throws InputError{
        ChessPiece.PieceType promotionPiece = switch (thirdParam) {
            case "q" -> QUEEN;
            case "n" -> KNIGHT;
            case "b" -> BISHOP;
            case "r" -> ROOK;
            default -> throw new InputError("invalid input");
        };
        return promotionPiece;
    }

}
