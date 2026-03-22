package taskmanagementsystem_sde2;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TaskManagementFacade {
    private final Map<String, Task> taskStore = new ConcurrentHashMap<>();
    private final Map<String, User> userStore = new ConcurrentHashMap<>();
    private final AuditObserver auditObserver;

    public TaskManagementFacade() {
        this.auditObserver = new AuditObserver();
    }

    public User registerUser(String name) {
        User user = new User(UUID.randomUUID().toString(), name);
        userStore.put(user.getId(), user);
        return user;
    }

    public Task createTask(String title, String description, int priority, LocalDate dueDate, User assignee) {
        String id = "TASK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Task task = new Task(id, title, description, priority, dueDate, assignee);
        taskStore.put(id, task);
        auditObserver.recordTransition(id, "SYSTEM", "N/A", "PENDING");
        return task;
    }

    /** SDE3: Transition guarded by the State machine. */
    public void startTask(String taskId, User triggeredBy) {
        Task task = taskStore.get(taskId);
        if (task == null) throw new IllegalArgumentException("Task not found: " + taskId);
        String before = task.getStatus();
        task.startProgress();
        auditObserver.recordTransition(taskId, triggeredBy.getName(), before, task.getStatus());
    }

    public void completeTask(String taskId, User triggeredBy) {
        Task task = taskStore.get(taskId);
        if (task == null) throw new IllegalArgumentException("Task not found: " + taskId);
        String before = task.getStatus();
        task.complete();
        auditObserver.recordTransition(taskId, triggeredBy.getName(), before, task.getStatus());
    }

    public void reopenTask(String taskId, User triggeredBy) {
        Task task = taskStore.get(taskId);
        if (task == null) throw new IllegalArgumentException("Task not found: " + taskId);
        String before = task.getStatus();
        task.reopen();
        auditObserver.recordTransition(taskId, triggeredBy.getName(), before, task.getStatus());
    }

    /** SDE3: Parallel stream search replaces linear O(N) for loop. */
    public List<Task> searchTasks(String keyword) {
        return taskStore.values().parallelStream()
                .filter(t -> t.getTitle().contains(keyword) || t.getDescription().contains(keyword))
                .collect(Collectors.toList());
    }

    public List<Task> filterByStatus(String status) {
        return taskStore.values().parallelStream()
                .filter(t -> t.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<TaskHistoryEntry> getAuditLog() {
        return auditObserver.getAuditLog();
    }

    public void shutdown() { auditObserver.shutdown(); }
}
