package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import websocket.commands.UserGameCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class InGameClient implements Client{
    private ServerFacade server;
    private Repl repl;
    private ChessGame game;
    private ChessGame.TeamColor teamColor;

    public InGameClient(Repl repl) {
        this.server = repl.server;
        this.repl = repl;
        this.teamColor = repl.teamColor;
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
        if(params.length != 2) {
            throw new InputError("Invalid");
        }
        try {
            isValidSquareOnBoard(params[0]);
            isValidSquareOnBoard(params[1]);
            String boardString = boardToString(new BoardPrinter(),this.game, this.teamColor);
            // web socket call here
            return "you can successfully call make move" + "\n" + boardString;
        }catch(InputError e) {
            throw new InputError("Invalid Input: Make a move: \"move\" <START> <END>");
        }
    }

    public String resign() {
        // make a websocket call and return the string
        return null;
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
        // web socket command
//        UserGameCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE,this.);
        return "You have left the game \n";
    }
    public String redraw() {
        return boardToString(new BoardPrinter(), this.game, teamColor);
    }


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




}
