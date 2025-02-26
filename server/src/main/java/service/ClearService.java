package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import result.ClearResult;

public class ClearService {
    UserDAO users;
    AuthDAO auths;
    GameDAO games;

    public ClearService(UserDAO users, AuthDAO auths, GameDAO games){
        this.users = users;
        this.auths = auths;
        this.games = games;
    }



    public ClearResult clear() {
            users.clear();
            auths.clear();
            games.clear();

            return null;
    };

}
