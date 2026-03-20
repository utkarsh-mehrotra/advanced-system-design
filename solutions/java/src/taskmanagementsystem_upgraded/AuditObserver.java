package taskmanagementsystem_upgraded;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SDE3: Asynchronous Audit Observer.
 * Subscribes to task state-change events and records them out-of-band from the main workflow.
 */
public class AuditObserver {
    private final List<TaskHistoryEntry> auditLog;
    private final ExecutorService auditPool;

    public AuditObserver() {
        // CopyOnWriteArrayList: safe for concurrent writes + free reads
        this.auditLog = new CopyOnWriteArrayList<>();
        this.auditPool = Executors.newSingleThreadExecutor();
    }

    public void recordTransition(String taskId, String triggeredByUserId, String fromState, String toState) {
        auditPool.submit(() -> {
            TaskHistoryEntry entry = new TaskHistoryEntry(taskId, triggeredByUserId, fromState, toState);
            auditLog.add(entry);
            System.out.println(entry);
        });
    }

    public List<TaskHistoryEntry> getAuditLog() { return auditLog; }

    public void shutdown() { auditPool.shutdown(); }
}
