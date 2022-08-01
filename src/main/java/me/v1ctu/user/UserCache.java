package me.v1ctu.user;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserCache {

    private final Map<UUID, User> users = new HashMap<>();

    public void put(UUID uuid, User user) {
        users.put(uuid, user);
    }

    public void remove(UUID uuid) {
        users.remove(uuid);
    }

    public User getById(UUID uuid) {
        return users.get(uuid);
    }

    public User getByName(String name) {
        for(User user : users.values()) {
            if(user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

}
