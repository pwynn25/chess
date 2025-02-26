package server;

import dataaccess.*;
import handler.ClearHandler;
import service.ClearService;
import spark.*;

public class Server {

    public void Server() {
//      DAOs
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();
        GameDAO games = new MemoryGameDAO();

//      Services
        ClearService clearService = new ClearService(users,auths,games);
        clearService.clear();

//        Handlers
        ClearHandler clearHandler = new ClearHandler();
        clearHandler




    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.delete("/db",((request, response) -> (ClearHandler.clear()))) {
        };
//        Spark.delete("/session",) {
//
//        }
//        Spark.get("/game");
//        Spark.post("/game");
//        Spark.put("/game");
//        Spark.post("/session",);
//        Spark.post("/user",);




        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
