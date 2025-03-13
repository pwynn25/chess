package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.ExceptionResponse;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    private UserDAO users;
    private AuthDAO auths;

    public UserService(UserDAO users, AuthDAO auths){
        this.users = users;
        this.auths = auths;
    }



//     ***     Register   ***
    public RegisterResult register(RegisterRequest r) throws ExceptionResponse{
        UserData user = users.getUser(r.username());

        if (r.username() == null || r.email() == null || r.password() == null) {
            throw new ExceptionResponse(400, "Error: bad request");
        }
        if (user == null) {
            String hashedPassword = BCrypt.hashpw(r.password(), BCrypt.gensalt());
            UserData newUser = new UserData(r.username(), hashedPassword, r.email());
            users.createUser(newUser);
            String authToken = generateToken();
            AuthData authData = new AuthData(authToken,newUser.getUsername());
            auths.createAuth(authData);
            return new RegisterResult(newUser.getUsername(),authToken);
        }
        else {
            throw new ExceptionResponse(403, "Error: already taken");
        }
    }


//     *** Login ***
    public LoginResult login(LoginRequest l) throws ExceptionResponse{
        UserData user = users.getUser(l.username());
        if(user != null ) {
            boolean userVerified = verifyUser(l.password(),user.getPassword());
            if(userVerified) {
                String newAuthToken = generateToken();
                AuthData authData = new AuthData(newAuthToken, user.getUsername());
                auths.createAuth(authData);
                return new LoginResult(user.getUsername(), newAuthToken);
            }
            else {
                throw new ExceptionResponse(401,"Error: unauthorized");
            }
        }
        else{
            throw new ExceptionResponse(401,"Error: unauthorized");
        }
    }
    boolean verifyUser(String password,  String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
//  ***  Logout ***
    public LogoutResult logout(LogoutRequest l) throws ExceptionResponse{
        AuthData authData = auths.getAuth(l.authToken());
        if(authData != null ) {
            if(Objects.equals(l.authToken(), authData.getAuth())) {
                auths.deleteAuth(authData.getAuth());
                return new LogoutResult();
            }
            else {
                throw new ExceptionResponse(401,"Error: unauthorized");
            }
        }
        else {
                throw new ExceptionResponse(401,"Error: unauthorized");
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
