package es.urjc;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper;
    private final RoomController roomController;

    public ReactiveWebSocketHandler(ObjectMapper objectMapper, RoomController roomController) {
        this.objectMapper = objectMapper;
        this.roomController = roomController;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        return session.receive()
                .map(WebSocketMessage::getPayload)
                .map(this::deserializeToObject)
                .doOnNext(user -> roomController.addUser(session.getId(), user))
                .doOnTerminate(() -> roomController.deleteUser(session.getId()))
                .then();
    }

    private User deserializeToObject(DataBuffer dataBuffer) {

        try {

            User user = objectMapper.readValue(dataBuffer.asInputStream(), User.class);
            String username = getUsername(user);
            user.setUsername(username);

            return user;

        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    private String getUsername(User user) {
        String id = user.getId().toString().split("-")[0];
        return user.getName().concat(id).toUpperCase();
    }
}
