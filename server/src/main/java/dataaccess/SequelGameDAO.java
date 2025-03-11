package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

import static dataaccess.DatabaseManager.createDatabase;

public class SequelGameDAO implements GameDAO{
    public SequelGameDAO() {
        try {
            createDatabase();
        }catch (DataAccessException e) {
                throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public GameData createGame(String gameName) {
        return null;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGameWhiteUsername(String username, int gameID) {

    }

    @Override
    public void updateGameBlackUsername(String username, int gameID) {

    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameData (
              `GameID` int NOT NULL,
              `WhiteUsername` varchar(256),
              `Blackusername` varchar(256),
              `GameName` varchar(256) NOT NULL,
              `ChessGame` varchar(256) NOT NULL,
              PRIMARY KEY (`GameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

}
