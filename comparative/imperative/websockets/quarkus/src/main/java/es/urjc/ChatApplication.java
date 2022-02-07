package es.urjc;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat/{username}")
@ApplicationScoped
public class ChatApplication {

    private Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        broadcast("> '" + username + "' connected");
        sessions.put(username, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        broadcast("< '" + username + "' disconnected");
        sessions.remove(username);
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        broadcast("* '" + username + "' left on error: " + throwable);
        sessions.remove(username);
    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("username") String username) {
        broadcast(username + ": " + message);
    }

    private void broadcast(String message) {
        sessions.values().forEach(session -> sendMessage(session, message));
    }

    private void sendMessage(Session session, String message){
        session.getAsyncRemote().sendObject(message, result -> {
            if (result.getException() != null) {
                System.out.println("Unable to send message: " + result.getException());
            }
        });
    }
}
