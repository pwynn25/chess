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
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class PostLoginClient implements Client{
    private final ServerFacade server;
    private final Repl repl;
    private HashMap<Integer, Integer> gameMap;


    public PostLoginClient(Repl repl) {
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
                case "logout" -> logout();
                case"create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "quit"-> quit();
                default -> help();
            };
        }
        catch(InputError e) {
            return e.getErrorMessage() + "\n";
        }

    }
    private String quit() {
        return "quit";
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
                sb.append(gameCounter).append(" Game Name: ").append(game.getGameName())
                        .append(" White Player: ").append(game.getWhiteUsername())
                        .append(" Black Player: ").append(game.getBlackUsername());
                sb.append("\n");
                gameMap.put(gameCounter, game.getGameID());
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
                int gameID = 0;
                int gameNum;
                ChessGame.TeamColor teamColor;
                try {
                    gameNum = Integer.parseInt(params[0]);
                    gameID = gameMap.get(gameNum);
                    if (gameID == 0) {
                        throw new InputError("The game you specified does not exist, please try again.");
                    }
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
                String boardString = boardToString(new BoardPrinter(),joinResult.game().getGame(), teamColor);

                return "You joined game " + gameNum + "\n" + boardString;
            }
            else {
                throw new InputError("Join a game and begin playing: \"join\" <ID> [WHITE | BLACK]\n");
            }
        }
        catch (ServerException e ) {
                return e.getMessage() + "\n";
        }

    }
    private String boardToString(BoardPrinter printer, ChessGame game, ChessGame.TeamColor teamColor) {
        return printer.printBoard(game.getBoard(),teamColor);
    }


    private String observe(String...params) throws InputError{
        Collection<GameData> games;
        int gameIDToObserve;
        if(params.length != 1) {
            throw new InputError("Observe a game without playing: \"observe\" <ID>");
        }
        try{
            try {
                gameIDToObserve = gameMap.get(Integer.parseInt(params[0]));
            }
            catch (NumberFormatException e) {
                throw new InputError("Observe a game without playing: \"observe\" <ID>");
            }
            catch (NullPointerException e) {
                throw new InputError("That game does not exist");
            }
            ListResult listResult= server.list(new ListRequest());
            games = listResult.games();

            for(GameData game: games) {
                if(game.getGameID() == gameIDToObserve) {
                    String boardString = boardToString(new BoardPrinter(),game.getGame(), WHITE);
                    return "You are observing game " + params[0] + "\n" + boardString;
                }
            }
            throw new InputError("That game does not exist");
        }
        catch(ServerException e) {
            return e.getMessage() + "\n";
        }
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
