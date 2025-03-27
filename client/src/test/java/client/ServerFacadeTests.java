package client;

import dataaccess.*;
import exception.ExceptionResponse;
import org.junit.jupiter.api.*;
import request.*;
import result.*;
import server.Server;
import ui.ServerException;
import ui.ServerFacade;

import static chess.ChessGame.TeamColor.WHITE;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
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
    @Test
    public void positiveLogout() throws ServerException {
        RegisterResult res = facade.register("pwynn","mama","pwynn@rutgers.com");
        LogoutRequest logReq = new LogoutRequest(res.authToken());

        facade.logout(logReq);
        try {
            CreateRequest createReq = new CreateRequest("jim");
            facade.create(createReq);

        } catch (ServerException e){
            assertEquals(401,e.getStatusCode());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void negativeLogout() throws ServerException {
        RegisterResult res = facade.register("pwynn","mama","pwynn@rutgers.com");
        LogoutRequest logReq = new LogoutRequest(res.authToken());
        facade.logout(logReq);
        try {
            facade.logout(logReq);
        } catch (ServerException e){
            assertEquals(401,e.getStatusCode());
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void positiveLogin() throws ServerException {
        RegisterResult res = facade.register("pwynn","mama","pwynn@rutgers.com");
        LoginRequest logReq = new LoginRequest("pwynn","mama");
        try {
            LoginResult loginRes = facade.login(logReq);
            assertNotEquals(res.authToken(),loginRes.authToken());
        } catch (ServerException e){
            fail();
        }
    }

    @Test
    public void negativeLogin() throws ServerException {
        RegisterResult res = facade.register("pwynn","mama","pwynn@rutgers.com");
        LoginRequest logReq = new LoginRequest("pwynn","mom");
        try {
            facade.login(logReq);
        } catch (ServerException e){
            assertEquals(401,e.getStatusCode());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void positiveCreate() throws ServerException {
        facade.register("pwynn","mama","pwynn@rutgers.com");
        CreateRequest createReq = new CreateRequest("jim");
        try {
            CreateResult createResult = facade.create(createReq);
            assertTrue(createResult.gameID() > 0);
        } catch (ServerException e){
            fail();
        }
    }

    @Test
    public void negCreate() throws ServerException {
        facade.register("pwynn","mama","pwynn@rutgers.com");
        CreateRequest createReq = new CreateRequest("jim");
        CreateRequest createReq1 = new CreateRequest("");
        try {
            CreateResult createResult = facade.create(createReq);
            assertTrue(createResult.gameID() > 0);
            facade.create(createReq1);
            fail();
        } catch (ServerException e){
            assertEquals(400,e.getStatusCode());
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void posJoin() throws ServerException {
        facade.register("pwynn","mama","pwynn@rutgers.com");
        CreateRequest createReq = new CreateRequest("jim");
        CreateRequest createReq1 = new CreateRequest("");
        try {
            CreateResult createResult = facade.create(createReq);
            assertTrue(createResult.gameID() > 0);
            JoinRequest joinReq = new JoinRequest(createResult.gameID(),WHITE);
            JoinResult joinRes = facade.join(joinReq);
            assertTrue(joinRes.game().getGameID() > 0);
        } catch (ServerException e){
            fail();
        }
    }
    @Test
    public void negJoin() throws ServerException {
        facade.register("pwynn","mama","pwynn@rutgers.com");
        CreateRequest createReq = new CreateRequest("jim");
        CreateRequest createReq1 = new CreateRequest("");
        try {
            CreateResult createResult = facade.create(createReq);
            assertTrue(createResult.gameID() > 0);
            JoinRequest joinReq = new JoinRequest(createResult.gameID(),WHITE);
            facade.join(joinReq);
            facade.join(joinReq);
            fail();
        } catch (ServerException e){
            assertEquals(403,e.getStatusCode());
            System.out.println(e.getMessage());
        }
    }
    @Test
    @DisplayName("ListGames")
    public void posList() throws ServerException {
        facade.register("pwynn","mama","pwynn@rutgers.com");
        CreateRequest createReq = new CreateRequest("jim");
        CreateRequest createReq1 = new CreateRequest("mom");
        try {
            facade.create(createReq);
            facade.create(createReq1);
            ListRequest reqL = new ListRequest();
            ListResult listResult = facade.list(reqL);
            assertEquals(2,listResult.games().size());
            System.out.println(listResult);
        } catch (ServerException e){
            System.out.println(e.getStatusCode());
            System.out.println(e.getMessage());

        }
    }
    @Test
    @DisplayName("ListGames")
    public void negList() throws ServerException {
        facade.register("pwynn","mama","pwynn@rutgers.com");
        CreateRequest createReq = new CreateRequest("jim");
        CreateRequest createReq1 = new CreateRequest("mom");
        try {
            ListRequest reqL = new ListRequest();
            ListResult listResult = facade.list(reqL);
            assertEquals(0,listResult.games().size());
            System.out.println(listResult);
        } catch (ServerException e){
            System.out.println(e.getStatusCode());
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
