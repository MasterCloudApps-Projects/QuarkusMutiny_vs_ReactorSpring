package es.urjc;


import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RoomWebSocket {

    private final RoomResource roomResource;

    @Inject
    public RoomWebSocket(RoomResource roomResource) {
        this.roomResource = roomResource;
    }

    @Incoming("incoming-process-user")
    @Outgoing("outgoing-process-user")
    public Uni<User> processNewUser(User newUser) {
        return Uni.createFrom().item(newUser)
                .map(user -> {
                    String username = getUsername(user);
                    return new User(user.getId(), user.getName(), username);
                })
                .invoke(user -> System.out.println("Processed user: " + user));
    }

    @Incoming("connect")
    public Uni<Void> connectUser(User user) {
        return Uni.createFrom().item(user)
                .onItem().invoke(roomResource::addUser)
                .onItem().ignore().andContinueWithNull();
    }

    @Incoming("disconnect")
    public Uni<Void> disconnectUser(User user) {
        return Uni.createFrom().item(user)
                .onItem().invoke(roomResource::deleteUser)
                .onItem().ignore().andContinueWithNull();
    }

    private String getUsername(User user) {
        String id = user.getId().toString().split("-")[0];
        return user.getName().concat(id).toUpperCase();
    }

}
