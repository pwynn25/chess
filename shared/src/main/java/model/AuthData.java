package model;

import java.util.Objects;

public class AuthData{
    private final String authToken;
    private final String username;

    public AuthData(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }
    public String getAuth() {
        return authToken;
    }
    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthData authData = (AuthData) o;
        return Objects.equals(authToken, authData.authToken) && Objects.equals(username, authData.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, username);
    }

    @Override
    public String toString() {
        return "AuthData{" +
                "authToken='" + authToken + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
