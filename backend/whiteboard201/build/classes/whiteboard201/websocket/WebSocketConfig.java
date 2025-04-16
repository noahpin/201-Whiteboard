package whiteboard201.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

// sets up a WebSocket server endpoint at /draw. User connects to this endpoint.

@ServerEndpoint("/draw")
public class DrawWebSocket {

    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("New session connected: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message received: " + message);
        // You can parse this into JSON if needed
        broadcast(message, session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("Session closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("Error on session " + session.getId() + ": " + error.getMessage());
    }

    private void broadcast(String message, Session sender) {
        for (Session session : sessions) {
            if (session.isOpen() && !session.equals(sender)) {
                session.getAsyncRemote().sendText(message); // Non-blocking
            }
        }
    }
}