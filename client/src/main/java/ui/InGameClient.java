package ui;

import chess.ChessGame;
import java.util.Arrays;


public class InGameClient implements Client{
    private ServerFacade server;
    private Repl repl;
    private ChessGame game;
    private ChessGame.TeamColor teamColor;

    public InGameClient(Repl repl) {
        this.server = repl.server;
        this.repl = repl;
    }

    public void setGame(ChessGame game) {
        this.game = game;

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
                case "highlight" -> highlight();
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
            return "you can successfully call make move" + "\n" + boardString;
        }catch(InputError e) {
            throw new InputError("Invalid Input: Make a move: \"move\" <START> <END>");
        }
    }

    public String resign() {
        return "The game is over";
    }

    public String highlight(String...params) throws InputError{
        if(params.length != 1) {
            throw new InputError("Highlight moves: \"hl\" <START>");
        }
        try {
            isValidSquareOnBoard(params[0]);
            return "you can successfully call highlight";
        }catch(InputError e) {
            throw new InputError("Highlight moves: \"hl\" <START>");
        }
    }
    public String leave() {
        repl.setUserStatus(Repl.UserStatus.LOGGED_IN);
        return "You have left the game \n";
    }
    public String redraw() {
        return null;
    }
    public void isValidSquareOnBoard(String param) throws InputError{
        int rowNum;
        if(2 == param.length()) {
            char[] chars = param.toCharArray();
            char column = chars[0];
            char row = chars[1];
            if(Character.isDigit(row)) {
                rowNum = row - '0';
                if(rowNum < 1 || rowNum > 8) {
                    throw new InputError("invalid input");
                }
            }
            else {
                throw new InputError("invalid input");
            }
            if (Character.isAlphabetic(column)) {
                char lowerCaseColumn = Character.toLowerCase(column);
                switch (lowerCaseColumn) {
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f':
                    case 'g':
                    case 'h':
                        // Valid column, do nothing
                        break;
                    default:
                        throw new InputError("invalid input");
                }
            }
            else {
                throw new InputError("invalid input");
            }
        }
        else {
            throw new InputError("invalid input");
        }
    }
    public String boardToString(BoardPrinter printer, ChessGame game, ChessGame.TeamColor teamColor) {
        return printer.printBoard(game.getBoard(),teamColor);
    }




}
