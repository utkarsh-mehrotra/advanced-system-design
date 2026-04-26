package musicstreamingservice_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
    private final Map<String, User> users;

    public UserManager() {
        users = new ConcurrentHashMap<>();
    }

    public void registerUser(User user) {
        users.put(user.getId(), user);
    }

    public User loginUser(String username, String password) {
        // Optimized to use values stream
        return users.values().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }
}
