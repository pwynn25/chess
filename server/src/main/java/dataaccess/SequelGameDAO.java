package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ExceptionResponse;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;


import static dataaccess.DatabaseManager.createDatabase;

public class SequelGameDAO implements GameDAO{
    private int gameID = 1;
    private final String createGame = "INSERT INTO GameData (GameID, GameName, ChessGame) VALUES ( ?, ?, ?);";
    private final String getGame = "SELECT * FROM GameData WHERE GameID = ?;";
    private final String listGames = "SELECT * FROM GameData";
    private final String upDateUsernameWhite = """
            UPDATE GameData
            SET WhiteUsername = ?
            WHERE GameID = ?;""";
    private final String upDateUsernameBlack = """
            UPDATE GameData
            SET BlackUsername = ?
            WHERE GameID = ?;""";


    public SequelGameDAO() {
        try {
            createDatabase();
        }catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void clear() throws ExceptionResponse{
        try(var conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE GameData;";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }catch(DataAccessException | SQLException e) {
            throw new ExceptionResponse(500, e.getMessage());
        }
    }

    @Override
    public GameData createGame(String gameName) throws ExceptionResponse{
        ChessGame game = new ChessGame();
        var serializer = new Gson();
        var gameJSON = serializer.toJson(game);
        int currentGameID = gameID;

        if(Objects.equals(gameName, "")) {
            throw new ExceptionResponse(400, "Error: bad request");
        }
        try(var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(createGame)){
                stmt.setInt(1, currentGameID);  // Set username
                stmt.setString(2, gameName);  // Set password
                stmt.setString(3, gameJSON);
                stmt.executeUpdate();

                gameID++;
            } catch (SQLException e) {
                throw new RuntimeException(e);
//                throw new ExceptionResponse(500,e.getMessage());
            }
        } catch (SQLException | DataAccessException e) {
            throw new ExceptionResponse(500, "There was an Error accessing the database");
        }
        return new GameData(currentGameID, gameName);
    }

    @Override
    public GameData getGame(int gameID) throws ExceptionResponse{
        GameData gameData;
        try(var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(getGame)) {
                stmt.setInt(1, gameID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int ID = rs.getInt(1);
                        String whiteUsername = rs.getString(2);
                        String blackUsername = rs.getString(3);
                        String gameName = rs.getString(4);
                        String gameJSON = rs.getString(5);
                        gameData = new GameData(ID, gameName);
                        gameData.setBlackUsername(blackUsername);
                        gameData.setWhiteUsername(whiteUsername);
                        var serializer = new Gson();
                        gameData.setGame(serializer.fromJson(gameJSON, ChessGame.class));
                    }
                    else {
                        return null;
                    }

                }
            } catch (SQLException e) {
                throw new ExceptionResponse(500, e.getMessage());
            }
        } catch (SQLException | DataAccessException e) {
            throw new ExceptionResponse(500, "There was an Error accessing the database");
        }
        return gameData;
    }

    @Override
    public Collection<GameData> listGames() throws ExceptionResponse{
        Collection<GameData> games = new ArrayList<>();
        try(var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(listGames)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int ID = rs.getInt(1);
                        String whiteUsername = rs.getString(2);
                        String blackUsername = rs.getString(3);
                        String gameName = rs.getString(4);

                        GameData game = new GameData(ID, gameName);
                        game.setBlackUsername(blackUsername);
                        game.setWhiteUsername(whiteUsername);
                        games.add(game);
                    }
                } catch(NullPointerException e) {
                    throw new ExceptionResponse(500, e.getMessage());
                }
            } catch (SQLException e) {
                throw new ExceptionResponse(500, e.getMessage());
            }
        } catch (SQLException | DataAccessException e) {
            throw new ExceptionResponse(500, "There was an Error accessing the database");
        }
        return games;
    }

    @Override
    public void updateGameWhiteUsername(String username, int gameID) throws ExceptionResponse{
        updateUsernameHelper(username,gameID,"White");
    }

    @Override
    public void updateGameBlackUsername(String username, int gameID) throws ExceptionResponse{
        updateUsernameHelper(username,gameID,"Black");
    }

    public void updateUsernameHelper(String username, int gameID, String playerColor) throws ExceptionResponse{
        String updateUsername;
        if(Objects.equals(playerColor, "White")) {
            updateUsername = upDateUsernameWhite;
        } else if(Objects.equals(playerColor, "Black")) {
            updateUsername = upDateUsernameBlack;
        }
        else {
            throw new ExceptionResponse(401, "Error: invalid request");
        }
        try(var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(updateUsername)){
                stmt.setString(1, username);  // Set username
                stmt.setInt(2, gameID);  // Set password
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (SQLException | DataAccessException e) {
            throw new ExceptionResponse(500, "There was an Error accessing the database");
        }
    }

}
