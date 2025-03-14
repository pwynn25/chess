package dataaccess;

import exception.ExceptionResponse;

import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class DataAccessSequel {
    public final String checkNumRowsCommandAuths = "SELECT COUNT(*) FROM AuthData;";
    public final String checkNumRowsCommandUsers = "SELECT COUNT(*) FROM UserData;";
    public final String checkNumRowsCommandGames = "SELECT COUNT(*) FROM GameData;";

    public int numRows;

    public int checkNumRows(String tableName) throws ExceptionResponse {
        try (var conn = DatabaseManager.getConnection()) {
            DatabaseManager.useChess();
            String checkNumRowsCommand;
            if (tableName == "AuthData") {
                checkNumRowsCommand = checkNumRowsCommandAuths;
            } else if (tableName == "UserData") {
                checkNumRowsCommand = checkNumRowsCommandUsers;
            }else {
                checkNumRowsCommand = checkNumRowsCommandGames;
            }

            try (var stmt = conn.prepareStatement(checkNumRowsCommand)) {
                try(ResultSet rs = stmt.executeQuery()) {
                    if(rs.next()) {
                        numRows = rs.getInt(1);
                    }
                }
            }catch (SQLException e) {
                throw new ExceptionResponse(500, e.getMessage());
            }
        } catch (SQLException | DataAccessException e) {
            throw new ExceptionResponse(500, e.getMessage());
        }
        return numRows;
    }
}
