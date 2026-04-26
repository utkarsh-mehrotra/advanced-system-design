package taskmanagementsystem_sde3;

import java.time.LocalDate;

/**
 * SDE3: Task entity delegating all lifecycle changes to the current GoF TaskState.
 * Illegal transitions (e.g., PENDING → COMPLETED) throw immediately.
 */
public class Task {
    private final String id;
    private String title;
    private String description;
    private int priority;
    private LocalDate dueDate;
    private User assignedUser;

    // Delegates all state logic to the active state object
    private TaskState state;

    public Task(String id, String title, String description, int priority, LocalDate dueDate, User assignedUser) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.assignedUser = assignedUser;
        this.state = new PendingState();
    }

    public synchronized void startProgress() {
        this.state = state.startProgress(this);
    }

    public synchronized void complete() {
        this.state = state.complete(this);
    }

    public synchronized void reopen() {
        this.state = state.reopen(this);
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getPriority() { return priority; }
    public LocalDate getDueDate() { return dueDate; }
    public User getAssignedUser() { return assignedUser; }
    public String getStatus() { return state.getStatusName(); }

    // Allow re-assignment
    public synchronized void reassignTo(User newUser) { this.assignedUser = newUser; }
    public synchronized void updateDetails(String title, String description, int priority, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
    }
}
