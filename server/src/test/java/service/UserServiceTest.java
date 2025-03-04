package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import exception.ExceptionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserServiceTest {

    public List<Object> registerThreeUsers() throws ExceptionResponse{
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();

        List<Object> list = new ArrayList<>();
        list.add(users);
        list.add(auths);

        String username1 = "pwynn25";
        String password1 = "pw264";
        String email1 = "crazyemail";

        String username2 = "brynn";
        String password2 = "Elizabeth";
        String email2 = "jimbob";

        String username3 = "Nick";
        String password3 = "21";
        String email3 = "yuhh";

        RegisterRequest a = new RegisterRequest(username1, password1,email1);
        RegisterRequest b = new RegisterRequest(username2, password2,email2);
        RegisterRequest c = new RegisterRequest(username3, password3,email3);

        UserService userService = new UserService(users,auths);

        userService.register(a);
        userService.register(b);
        userService.register(c);


        return list;
    }
    @Test
    @DisplayName("Password null")
    void registerNegative(){
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();


        String username = "pwynn25";
        String password = null;
        String email = "jimmydean@gmail.com";

        RegisterRequest r = new RegisterRequest(username, password,email);
        UserService userService = new UserService(users,auths);

        ExceptionResponse res = assertThrows(ExceptionResponse.class,()->userService.register(r));

        assertEquals(400,res.getStatusCode());
        assertEquals("Error: bad request", res.getMessage());

    }
    @Test
    @DisplayName("registerPositive")
    void registerPositive() throws ExceptionResponse {
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();

        String username = "pwynn25";
        String password = "131878";
        String email = "jimmydean@gmail.com";
        RegisterRequest r = new RegisterRequest(username, password,email);
        UserService userService = new UserService(users,auths);
        userService.register(r);


        assertEquals(username,users.getUser(username).getUsername());
        assertNotNull(users.getUser(username).getPassword(),"Password not returned");

    }

    @Test
    @DisplayName("register and login")
    void loginPositive() throws ExceptionResponse{
        List<Object> list = registerThreeUsers();

        UserDAO retrievedUsers = (UserDAO) list.get(0);  // Casting back to UserDAO
        AuthDAO retrievedAuths = (AuthDAO) list.get(1);

        UserService userService = new UserService(retrievedUsers,retrievedAuths);

        LoginResult l = userService.login(new LoginRequest("pwynn25","pw264"));
        LoginResult d = userService.login(new LoginRequest("pwynn25","pw264"));


        String authToTest = l.authToken();
        assertNotEquals(l.authToken(),d.authToken());
        assertEquals("pwynn25",retrievedAuths.getAuth(authToTest).getUsername());
    }
    @Test
    void loginNegative() throws ExceptionResponse{

        List<Object> list = registerThreeUsers();

        UserDAO retrievedUsers = (UserDAO) list.get(0);  // Casting back to UserDAO
        AuthDAO retrievedAuths = (AuthDAO) list.get(1);

        UserService userService = new UserService(retrievedUsers,retrievedAuths);


        LoginRequest l = new LoginRequest("pwynn25","pw264");
        LoginRequest d = new LoginRequest("brynn","Elizabeth");
        userService.login(l);
        userService.login(d);

        try {
            LoginRequest e = new LoginRequest("Nick","2602");
            userService.login(e);
        } catch (ExceptionResponse e ) {
            assertEquals(401,e.getStatusCode());
            assertEquals("Error: unauthorized", e.getMessage());
        }

    }

    @Test
    void logoutPositive() throws ExceptionResponse{
// Register one user then try and log them out;
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();

        String username1 = "pwynn25";
        String password1 = "pw264";
        String email1 = "jimmydean@gmail.com";

        RegisterRequest a = new RegisterRequest(username1, password1,email1);
        UserService userService = new UserService(users,auths);
        RegisterResult res = userService.register(a);
        assertNotNull(auths.getAuth(res.authToken()));



        LogoutRequest l = new LogoutRequest(res.authToken());

        LogoutResult resL = userService.logout(l);


        assertNull(auths.getAuth(l.authToken()));


    }

    @Test
    void logoutNegative() throws ExceptionResponse{
        // Register one user then try and log them out;
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();

        String username1 = "pwynn25";
        String password1 = "pw264";
        String email1 = "jimmydean@gmail.com";

        RegisterRequest a = new RegisterRequest(username1, password1,email1);
        UserService userService = new UserService(users,auths);
        RegisterResult res = userService.register(a);
        assertNotNull(auths.getAuth(res.authToken()));


        try {
            LogoutRequest e = new LogoutRequest("incorrectauthtoken");
            userService.logout(e);
        } catch (ExceptionResponse e ) {
            assertEquals(401,e.getStatusCode());
            assertEquals("Error: unauthorized", e.getMessage());
        }
        assertNotNull(auths.getAuth(res.authToken()));
    }

}