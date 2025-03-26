package ui;

import chess.ChessGame;
import model.GameData;
import request.CreateRequest;
import request.JoinRequest;
import request.ListRequest;
import request.LogoutRequest;
import result.JoinResult;
import result.ListResult;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class PostLoginClient implements Client{
    private final ServerFacade server;
    private final Repl repl;

    public PostLoginClient( Repl repl) {
        this.server = repl.server;
        this.repl = repl;
    }
    public String eval(String input){
        try {
            var tokens = input.split(" ");
            var cmd = tokens[0];
            var params = Arrays.copyOfRange(tokens,1,tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case"create" -> create(params);
                case "list" -> list();
                case "join" -> join();
                case "observe" -> observe(params);
                case "quit"-> "quit\n";
                default -> help();
            };
        }
        catch(InputError e) {
            return e.getErrorMessage() + "\n";
        }

    }

    private String logout(){
        try {
            server.logout(new LogoutRequest("auth"));
            repl.setUserStatus(UserStatus.userStatus.LOGGED_OUT);
            return "You successfully logged out. \n";
        }
        catch(ServerException e) {
            return e.getMessage() + "\n";
        }
    }
    private String create(String...params) throws InputError{
        try {
            if(params.length == 1) {
                server.create(new CreateRequest(params[0]));
                return "Your game was successfully created!\n";
            }
            else {
                throw new InputError("Create a new game: \"create\" <GAMENAME>\n");
            }
        }
        catch(ServerException e) {
            return e.getMessage() + "\n";
        }
    }
    private String list() {
        try {
            int gameCounter = 1;
            ListResult listResult= server.list(new ListRequest());
            StringBuilder sb = new StringBuilder();
            for (GameData game : listResult.games()) {
                sb.append(gameCounter).append(" Game ID: ").append(game.getGameID())
                        .append(" White Player: ").append(game.getWhiteUsername())
                        .append(" Black Player: ").append(game.getBlackUsername());
                sb.append("\n");
                gameCounter++;
            }
            String gameList = sb.toString();
            return gameList + "\n";
        }
        catch(ServerException e) {
            return e.getMessage() + "\n";
        }
    }
    private String join(String...params) throws InputError{
        try{
            if(params.length ==2) {
                int gameID;
                ChessGame.TeamColor teamColor;
                try {
                    gameID = Integer.parseInt(params[0]);
                }
                catch (NumberFormatException e) {
                    throw new InputError("Join a game and begin playing: \"join\" <ID> [WHITE | BLACK]\n");
                }
                if (Objects.equals(params[1], "white")) {
                    teamColor = WHITE;
                }
                else if (Objects.equals(params[1], "black")) {
                    teamColor = BLACK;
                }
                else {
                    throw new InputError("Join a game and begin playing: \"join\" <ID> [WHITE | BLACK]\n");
                }
                JoinResult joinResult = server.join(new JoinRequest(gameID, teamColor));
                return joinResult.toString() + "\n";
            }
            else {
                throw new InputError("Join a game and begin playing: \"join\" <ID> [WHITE | BLACK]\n");
            }
        }
        catch (ServerException e ) {
                return e.getMessage() + "\n";
        }

    }
    private String observe(String...params) throws InputError{
        return "This feature has yet to be implemented! \n";
    }


    private String help() {
        return """
                    Options:
                    Join a game and begin playing: "join" <ID> [WHITE | BLACK]
                    logout: "r", "register" <USERNAME> <PASSWORD> <EMAIL>
                    Create a new game: "create" <GAMENAME>
                    List games: "list"
                    quit: "quit"
                    help: "help"
               """;
    }

}
