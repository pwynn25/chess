
import ui.Repl;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8086";

        new Repl(serverUrl).run();

    }
}