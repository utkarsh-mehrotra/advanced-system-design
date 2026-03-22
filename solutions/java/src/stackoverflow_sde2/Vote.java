package stackoverflow_sde2;

public class Vote {
    private final User user;
    private final int value; // 1 for UP, -1 for DOWN

    public Vote(User user, int value) {
        if (value != 1 && value != -1) {
            throw new IllegalArgumentException("Vote must be +1 or -1");
        }
        this.user = user;
        this.value = value;
    }

    public User getUser() { return user; }
    public int getValue() { return value; }
}
