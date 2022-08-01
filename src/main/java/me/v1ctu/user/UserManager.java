package me.v1ctu.user;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class UserManager {

    private final UserCache userCache;
    private final UserRepository userRepository;

    public void handleLogin(UUID uuid, String name) {
        User user = userRepository.getById(uuid);
        if (user == null) {
            userCache.put(uuid, new User(uuid, name, 0));
        }
        if (userCache.getById(uuid) == null) {
            userCache.put(uuid, user);
        }
    }

    public void handleQuit(UUID uuid, User user) {
        if (userCache.getById(uuid) != null) {
            userCache.remove(uuid);
            userRepository.replace(user);
        }
    }

    public User getById(UUID uuid) {
        final User user = userCache.getById(uuid);
        return user != null ? user : userRepository.getById(uuid);
    }

    public User getByName(String name) {
        final User user = userCache.getByName(name);
        return user != null ? user : userRepository.getByName(name);
    }

    public void depositById(UUID uuid, double amount) {
        User user = userCache.getById(uuid);
        if (user != null) {
            user.setBalance(user.getBalance() + amount);
        }
        userRepository.replace(user);
    }

    public void setById(UUID uuid, double amount) {
        User user = userCache.getById(uuid);
        if (user != null) {
            user.setBalance(amount);
        }
        userRepository.replace(user);
    }

    public void withdrawById(UUID uuid, double amount) {
        User user = userCache.getById(uuid);
        if (user != null) {
            user.setBalance(user.getBalance() - amount);
        }
        userRepository.replace(user);
    }

}
