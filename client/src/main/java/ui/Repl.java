package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.Repl.UserStatus.LOGGED_OUT;

public class Repl implements GameHandler{
    public String serverURL;
    private Client client;
    private PostLoginClient postClient;
    private PreLoginClient preClient;
    private InGameClient inGameClient;
    private UserStatus userStatus;
    public ServerFacade server;
    public HashMap<Integer, Integer> gameMap = new HashMap<>();
    public ChessGame.TeamColor teamColor;

    public Repl(String serverURL) {
        this.serverURL = serverURL;
        this.userStatus = LOGGED_OUT;
        this.server = new ServerFacade(serverURL);
        this.client = new PreLoginClient(this);
        this.postClient = new PostLoginClient(this);
        this.preClient = new PreLoginClient(this);
        this.inGameClient = new InGameClient(this);

    }


    public void run() {
        System.out.println("Welcome to the game!");

        Scanner scanner = new Scanner(System.in);
        var result = "";

        System.out.println("Options:");
        System.out.println("Login as an existing user: \"login\" <USERNAME> <PASSWORD>");
        System.out.println("Register a new user: \"register\" <USERNAME> <PASSWORD> <EMAIL>");
        System.out.println("Exit the program: \"quit\"");
        System.out.println("Print this message: \"help\"");

        UserStatus current = userStatus;
        while (!result.equals("quit")) {
            String line = scanner.nextLine();
            switch (userStatus) {
                case LOGGED_IN -> client = postClient;
                case LOGGED_OUT -> client = preClient;
                case IN_GAME -> client = inGameClient;
            }
            result = client.eval(line);
            System.out.print(SET_TEXT_COLOR_BLUE + result);
        }

        System.out.println("\nThanks for playing");
        System.exit(0);
    }

    public void setUserStatus(UserStatus status) {
        this.userStatus = status;
    }
    public enum UserStatus {
        LOGGED_IN,
        IN_GAME,
        LOGGED_OUT
    }

    public void setGame(ChessGame game) {
        this.inGameClient.setGame(game);
                // function that sets the game
    }
    public void setTeamColor(ChessGame.TeamColor teamColor) {
        this.teamColor = teamColor;
    }
    public void printMessage(String message) {
        System.out.print(SET_TEXT_COLOR_RED + message);
    }
    public void updateGame(GameData game) {

    }
    public void addAuthTokenToClient(String authToken) {
        this.inGameClient.setAuthToken(authToken);
    }
    public void addGameIDToClient(int gameID) {
        this.inGameClient.setGameID(gameID);
    }
    public void establishWebSocketConnection() {
        this.inGameClient.establishConnection();
    }
}
