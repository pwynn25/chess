package server;

import dataaccess.*;
import exception.ExceptionResponse;
import handler.ClearHandler;
import handler.ExceptionHandler;
import handler.LoginHandler;
import handler.LogoutHandler;
import handler.RegisterHandler;
import service.ClearService;
import service.UserService;
import spark.*;

public class Server {
    private ClearHandler clearHandler;
    private RegisterHandler registerHandler;
    private LoginHandler loginHandler;
    private LogoutHandler logoutHandler;
    private ExceptionHandler exceptionHandler;

    public Server() {
//      DAOs
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();
        GameDAO games = new MemoryGameDAO();

//      Services
        ClearService clearService = new ClearService(users,auths,games);
        UserService userService = new UserService(users,auths);


//        Handlers
        this.clearHandler = new ClearHandler(clearService);
        this.registerHandler = new RegisterHandler(userService);
        this.loginHandler = new LoginHandler(userService);
        this.logoutHandler = new LogoutHandler(userService);
        this.exceptionHandler = new ExceptionHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
            //This line initializes the server and can be removed once you have a functioning endpoint
            Spark.delete("/db", (req, res) -> (clearHandler.clearDB(req, res)));
            Spark.delete("/session", (Request req, Response res) -> (logoutHandler.logout(req, res)));
//        Spark.get("/game");
//        Spark.post("/game");
//        Spark.put("/game");
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
