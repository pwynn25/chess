package dataaccess;

import model.AuthData;

import static dataaccess.DatabaseManager.createDatabase;

public class SequelAuthDAO implements AuthDAO{

    public SequelAuthDAO() {
        try{
            createDatabase();
        }catch(DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public void createAuth(AuthData authData) {

    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String username) {

    }
}
