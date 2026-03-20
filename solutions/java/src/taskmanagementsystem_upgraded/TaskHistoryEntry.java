package taskmanagementsystem_upgraded;

import java.time.Instant;

/** Immutable audit entry recording a state transition. */
public class TaskHistoryEntry {
    private final String taskId;
    private final String triggeredByUser;
    private final String fromState;
    private final String toState;
    private final Instant timestamp;

    public TaskHistoryEntry(String taskId, String triggeredByUser, String fromState, String toState) {
        this.taskId = taskId;
        this.triggeredByUser = triggeredByUser;
        this.fromState = fromState;
        this.toState = toState;
        this.timestamp = Instant.now();
    }

    @Override
    public String toString() {
        return String.format("[AUDIT] Task %s | %s → %s | by: %s | at: %s",
                taskId, fromState, toState, triggeredByUser, timestamp);
    }
}
