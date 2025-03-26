package ui;

public class InGameClient implements Client{
    private ServerFacade server;
    private Repl repl;

    public InGameClient(String urlServer, Repl repl) {
        this.server = new ServerFacade(urlServer);
        this.repl = repl;
    }
    public String eval(String input) {
        return null;
    }

}
