package ui;

import chess.ChessBoard;
import chess.ChessGame;

import java.util.HashMap;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.Repl.UserStatus.LOGGED_OUT;

public class Repl {
    private String urlServer;
    private Client client;
    private PostLoginClient postClient;
    private PreLoginClient preClient;
    private InGameClient inGameClient;
    private UserStatus userStatus;
    public ServerFacade server;
    public HashMap<Integer, Integer> gameMap = new HashMap<>();

    public Repl(String urlServer) {
        this.urlServer = urlServer;
        this.userStatus = LOGGED_OUT;
        this.server = new ServerFacade(urlServer);
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
}
