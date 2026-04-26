package stackoverflow_sde3;

public class User {
    private final int id;
    private final String username;
    private final String email;
    
    // SDE3: Kept simply as an atomic state internally, but modified strictly via ReputationService.
    private int reputation;

    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.reputation = 0;
    }

    public synchronized void updateReputation(int value) {
        this.reputation += value;
        if (this.reputation < 0) {
            this.reputation = 0;
        }
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public int getReputation() { return reputation; }
}
