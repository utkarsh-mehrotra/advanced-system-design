package taskmanagementsystem_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class Task {
    private final String taskId;
    private final AtomicReference<TaskStatus> status;

    public Task(String taskId) {
        this.taskId = taskId;
        this.status = new AtomicReference<>(TaskStatus.TODO);
    }

    public String getTaskId() { return taskId; }

    public boolean moveLane(TaskStatus expected, TaskStatus next) {
        if (status.compareAndSet(expected, next)) {
            EventBus.getInstance().publish("TASK_STATUS_CHANGED", taskId + ":" + next.name());
            return true;
        }
        return false;
    }
}
