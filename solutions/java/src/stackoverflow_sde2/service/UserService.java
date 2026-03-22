package stackoverflow_sde2.service;

import stackoverflow_sde2.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserService {
    private final Map<Integer, User> userRegistry;
    private final AtomicInteger userCounter;

    public UserService() {
        this.userRegistry = new ConcurrentHashMap<>();
        this.userCounter = new AtomicInteger(1);
    }

    public User createUser(String username, String email) {
        User user = new User(userCounter.getAndIncrement(), username, email);
        userRegistry.put(user.getId(), user);
        return user;
    }

    public User getUser(int id) {
        return userRegistry.get(id);
    }
}
