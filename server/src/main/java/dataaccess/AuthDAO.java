package dataaccess;

import exception.ExceptionResponse;
import model.AuthData;

public interface AuthDAO {
    void clear() throws ExceptionResponse;
    void createAuth(AuthData authData) throws ExceptionResponse;
    AuthData getAuth(String authToken) throws ExceptionResponse;
    void deleteAuth(String username)throws ExceptionResponse;


}
