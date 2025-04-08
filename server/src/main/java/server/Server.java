package server;

import dataaccess.*;
import exception.ExceptionResponse;
import org.eclipse.jetty.websocket.api.Session;
import handler.*;
import handler.ExceptionHandler;
import service.ClearService;
import service.GameService;
import service.PlayGameService;
import service.UserService;
import spark.*;

public class Server {
    private ClearHandler clearHandler;
    private RegisterHandler registerHandler;
    private LoginHandler loginHandler;
    private LogoutHandler logoutHandler;
    private ListGamesHandler listGamesHandler;
    private CreateGameHandler createGameHandler;
    private JoinGameHandler joinGameHandler;
    private ExceptionHandler exceptionHandler;
    private WebSocketHandler webSocketHandler;
    private WebSocketSessions webSocketSessions;

    public Server() {
//      DAOs

        UserDAO users = new SequelUserDAO();
        AuthDAO auths= new SequelAuthDAO();
        GameDAO games= new SequelGameDAO();



//      Services
        ClearService clearService = new ClearService(users,auths,games);
        UserService userService = new UserService(users,auths);
        GameService gameService = new GameService(users,auths,games);
        PlayGameService playGameService = new PlayGameService(users,auths,games);


//        Web Socket Sessions
        this.webSocketSessions = new WebSocketSessions();


//        Handlers
        this.clearHandler = new ClearHandler(clearService);
        this.registerHandler = new RegisterHandler(userService);
        this.loginHandler = new LoginHandler(userService);
        this.logoutHandler = new LogoutHandler(userService);
        this.listGamesHandler = new ListGamesHandler(gameService);
        this.createGameHandler = new CreateGameHandler(gameService);
        this.joinGameHandler = new JoinGameHandler(userService,gameService);
        this.exceptionHandler = new ExceptionHandler();
        this.webSocketHandler = new WebSocketHandler(playGameService, webSocketSessions);

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.webSocket("/ws",this.webSocketHandler);
        Spark.delete("/db", (req, res) -> (clearHandler.clearDB(req, res)));
        Spark.delete("/session", (Request req, Response res) -> (logoutHandler.logout(req, res)));
        Spark.get("/game", listGamesHandler::list);
        Spark.post("/game", (Request req, Response res) -> (createGameHandler.create(req, res)));
        Spark.put("/game",joinGameHandler::join);
        Spark.post("/session", (Request req, Response res) -> (loginHandler.login(req, res)));
        Spark.post("/user", (req, res) -> (registerHandler.register(req, res)));
        Spark.exception(ExceptionResponse.class,(e, req, res) -> exceptionHandler.handleException(e, req, res));


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
