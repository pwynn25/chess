package dataaccess;

import model.AuthData;

import java.sql.SQLException;

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
        try(var conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE AuthData;";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }catch(DataAccessException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
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
