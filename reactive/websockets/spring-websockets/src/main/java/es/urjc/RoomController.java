package es.urjc;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class RoomController {

    private static final Map<String, User> users = new HashMap<>();

    public void addUser(String sessionId, User user) {
        users.put(sessionId, user);
        System.out.println("Saved new user: " + user);
    }

    public void deleteUser(String sessionId) {
        User removedUser = users.remove(sessionId);
        System.out.println("Deleted new user: " + removedUser);
    }

    @GetMapping
    public Flux<User> getConnectedUsers() {
        return Flux.fromIterable(users.values());
    }

    @GetMapping("/size")
    public Mono<Integer> getNumberConnectedUsers() {
        return Mono.just(users.size());
    }

}
