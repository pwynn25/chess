package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    private Map<String, UserData> userDataMap = new HashMap<>();



    public void clear() {
        userDataMap.clear();
    }


    @Override
    public UserData getUser(String username) {
        return userDataMap.getOrDefault(username,null);
    }
    @Override
    public void createUser(UserData userData) {
        userDataMap.put(userData.getUsername(), userData);
    }


}
