package ui;

import request.LoginRequest;
import result.LoginResult;
import result.RegisterResult;

import java.util.Arrays;

public class PreLoginClient implements Client{
    private final ServerFacade server;
    private final Repl repl;

    public PreLoginClient(Repl repl) {
        this.server = repl.server;
        this.repl = repl;
    }


    public String eval(String input){
        try {
            var tokens = input.split(" ");
            var cmd = tokens[0];
            var params = Arrays.copyOfRange(tokens,1,tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case"login" -> login(params);
                case "quit" -> quit();
                default->help();
            };
        }
        catch(InputError e) {
            return e.getErrorMessage();
        }
    }

    private String register(String...params) throws InputError{
        if(params.length == 3) {
            try {
                RegisterResult regRes = server.register(params[0], params[1], params[2]);
                this.repl.setUserStatus(UserStatus.userStatus.LOGGED_IN);
                return regRes.username() + " has registered and logged in. \n";
            }
            catch (ServerException e) {
                return e.getMessage() + "\n";
            }
        }
        else {
            throw new InputError("Register a new user: \"register\" <USERNAME> <PASSWORD> <EMAIL>\n");
        }
    }
    private String login(String...params) throws InputError{
        if(params.length == 2) {
            LoginRequest logReq = new LoginRequest(params[0],params[1]);
            try {
                server.login(logReq);
                this.repl.setUserStatus(UserStatus.userStatus.LOGGED_IN);
                return "successful login\n";
            }
            catch (ServerException e) {

                return "Server error: " + e.getMessage() + "\n";
            }
        }
        else {
            throw new InputError("Login as an existing user: \"login\" <USERNAME> <PASSWORD>\n");
        }
    }
    private String quit() {
        return "quit\n";
    }

    public String help() {
        return """
                    Options:
                    Login as an existing user: "login" <USERNAME> <PASSWORD>
                    Register a new user: "register" <USERNAME> <PASSWORD> <EMAIL>
                    Exit the program: "quit"
                    Print this message: "help"
               """;
    }

}
