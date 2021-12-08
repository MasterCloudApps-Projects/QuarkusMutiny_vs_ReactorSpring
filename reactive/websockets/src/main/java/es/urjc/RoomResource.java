package es.urjc;


import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.*;

@Path("/users")
@ApplicationScoped
public class RoomResource {

    private static final Map<UUID, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getId(), user);
        System.out.println("Saved new user: " + user);
    }

    public void deleteUser(User user) {
        User removedUser = users.remove(user.getId());
        System.out.println("Deleted new user: " + removedUser);
    }

    @GET
    public Multi<User> getConnectedUsers() {
        return Multi.createFrom().iterable(users.values());
    }

    @GET
    @Path("/size")
    public Uni<Integer> getNumberConnectedUsers() {
        return Uni.createFrom().item(users.size());
    }

}
