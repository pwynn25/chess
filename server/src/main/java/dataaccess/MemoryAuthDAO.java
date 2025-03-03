package dataaccess;

import model.AuthData;

import java.util.*;

public class MemoryAuthDAO implements AuthDAO{

    private Map<String,AuthData> authDataMap = new HashMap<>();

    public void clear() {
        authDataMap.clear();
    }
    @Override
    public void createAuth(AuthData authData) {
        authDataMap.put(authData.getAuth(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authDataMap.getOrDefault(authToken,null);
    }

    @Override
    public void deleteAuth(String authToken) {
        authDataMap.remove(authToken);
    }
    public int getAuthMapSize() {
        return authDataMap.size();
    }












  // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemoryAuthDAO that = (MemoryAuthDAO) o;
        return Objects.equals(authDataMap, that.authDataMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(authDataMap);
    }
}
