package ui;

import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.UserStatus.userStatus.LOGGED_IN;
import static ui.UserStatus.userStatus.LOGGED_OUT;

public class Repl {
    private String urlServer;

    public Repl(String urlServer) {
        this.urlServer = urlServer;
    }

        private Client client;
        private UserStatus.userStatus userStatus;

        public Repl() {
            this.userStatus = LOGGED_OUT;
        }

        public void run() {
            System.out.println("Welcome to the game!");

            Scanner scanner = new Scanner(System.in);

            var result = "";

            while (!result.equals("quit")) {
                String line = scanner.nextLine();
                if (userStatus == LOGGED_OUT) {
                    client  = new PreLoginClient(line, urlServer);
                } else if (userStatus == LOGGED_IN) {
                    client = new PostLoginClient(line, urlServer);
                }

                try{
                    result = client.eval(line);
                    System.out.print(SET_TEXT_COLOR_BLUE + result);
                } catch {

                }


            }
        }
}
