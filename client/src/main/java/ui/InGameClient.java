package ui;

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
    public String eval(String input) {
        return null;
    }

}
