package client;

import dataaccess.*;
import exception.ExceptionResponse;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerException;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade = new ServerFacade("http://localhost:8080");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void positiveRegister() throws ServerException {
        var registerResult = facade.register("pwynn","mama","pwynn@rutgers.com");
        System.out.println(registerResult.authToken());
        assertTrue(registerResult.authToken().length() > 10);
    }
    @Test
    public void negativeRegister() throws ServerException {
        facade.register("pwynn","mama","pwynn@rutgers.com");
        try {
            facade.register("pwynn","mama","pwynn@rutgers.com");
            fail();
        } catch (ServerException e){
            assertEquals(403,e.getStatusCode());
            System.out.println(e.getMessage());
        }
    }
    @BeforeEach
    public void clear() {
        GameDAO games = new SequelGameDAO();
        UserDAO users = new SequelUserDAO();
        AuthDAO auths = new SequelAuthDAO();
        try {
            games.clear();
            users.clear();
            auths.clear();
        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

}
