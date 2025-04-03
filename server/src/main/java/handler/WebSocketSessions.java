package handler;

import javax.websocket.Session;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class WebSocketSessions {
    private Map<Integer, Set<Session>> sessionMap;

    public void addSessionToGame(int gameID, Session session) {
        Set<Session> playersAndObservers = sessionMap.get(gameID);
        playersAndObservers.add(session);
        this.sessionMap.put(gameID, playersAndObservers);
    }

    public void removeSessionFromGame(int gameID, Session session) {
        Set<Session> playersAndObservers = sessionMap.get(gameID);
        playersAndObservers.remove(session);
        this.sessionMap.put(gameID, playersAndObservers);
    }

    public void removeSession(Session session) {

   }

   public Set<Session> getSessionsForGame(int gameID) {
        return sessionMap.get(gameID);
    }


}
