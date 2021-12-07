package es.urjc;


import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String username;

    public User() {
    }

    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(UUID id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
