package ui;

import java.util.Arrays;
import java.util.HashMap;

public class InGameClient implements Client{
    private ServerFacade server;
    private Repl repl;
    private HashMap<Integer, Integer> gameMap;

    public InGameClient(Repl repl) {
        this.server = repl.server;
        this.repl = repl;
        this.gameMap = repl.gameMap;
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

    public String makeMove(String...params) {

    }

    public String resign() {

    }
    public String highlight() {

    }
    public String leave() {
        repl.setUserStatus(Repl.UserStatus.LOGGED_IN);
        return "You have left the game \n";
    }
    public String redraw() {

    }


}
