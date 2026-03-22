package splitwise_sde2;

import java.util.Objects;

public class User {
    private final String id;
    private final String name;
    private final String email;

    // SDE3: Balances are no longer stored in the User object. 
    // They are tracked centrally in a dedicated BalanceManager to prevent distributed locking issues.

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
