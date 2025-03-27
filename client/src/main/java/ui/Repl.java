package ui;

import java.util.HashMap;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.UserStatus.userStatus.LOGGED_OUT;

public class Repl {
    private String urlServer;
    private Client client;
    private UserStatus.userStatus userStatus;
    public ServerFacade server;
    public HashMap<Integer, Integer> gameMap = new HashMap<>();

    public Repl(String urlServer) {
        this.urlServer = urlServer;
        this.userStatus = LOGGED_OUT;
        this.server = new ServerFacade(urlServer);
        this.client = new PreLoginClient(this);
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

        while (!result.equals("quit")) {
            String line = scanner.nextLine();
            switch (userStatus) {
                case LOGGED_IN -> client = new PostLoginClient(this);
                case LOGGED_OUT -> client = new PreLoginClient(this);
                case IN_GAME -> client = new InGameClient(urlServer, this);
            }

            result = client.eval(line);
            System.out.print(SET_TEXT_COLOR_BLUE + result);
        }
        System.out.println("\nThanks for playing");
        System.exit(0);
    }
    public void setUserStatus(UserStatus.userStatus status) {
        this.userStatus = status;
    }
}
