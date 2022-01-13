package es.urjc;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ChatApplication {

    private Map<String, String> sessions = new ConcurrentHashMap<>();

    @MessageMapping("/connect/{username}")
    @SendTo("/topic/messages")
    public String onConnect(@DestinationVariable("username") String username,
                            StompHeaderAccessor stompHeaderAccessor) {

        sessions.put(stompHeaderAccessor.getSessionId(), username);
        return "> '" + username + "' connected";
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public String onMessage(StompHeaderAccessor stompHeaderAccessor, String message) {
        String username = sessions.get(stompHeaderAccessor.getSessionId());
        return username + ": " + message;
    }

    @MessageMapping("/disconnect")
    @SendTo("/topic/messages")
    public String onDisconnect(SimpMessageHeaderAccessor headerAccessor) {

        String username = sessions.get(headerAccessor.getSessionId());
        sessions.remove(headerAccessor.getSessionId());

        return "< '" + username + "' disconnected";
    }

}
