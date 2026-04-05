package taskmanagementsystem_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BoardManager {
    private final Map<String, Task> board = new ConcurrentHashMap<>();

    public void loadTask(Task task) {
        board.put(task.getTaskId(), task);
    }

    public void updateTask(String taskId, TaskStatus expected, TaskStatus next) {
        Task t = board.get(taskId);
        if (t != null) {
            if (!t.moveLane(expected, next)) {
                System.out.println("BoardManager: Edit collision detected on " + taskId);
            }
        }
    }
}
