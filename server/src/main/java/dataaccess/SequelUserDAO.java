package dataaccess;

import exception.ExceptionResponse;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import static dataaccess.DatabaseManager.createDatabase;

public class SequelUserDAO implements UserDAO {
    public SequelUserDAO()  {
        try {
            createDatabase();
        }catch(DataAccessException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createUser(UserData userData) {

    }
    private final String[] createUserDataTable = {
            """
            CREATE TABLE IF NOT EXISTS  UserData (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

}
