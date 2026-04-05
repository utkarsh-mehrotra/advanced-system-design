package taskmanagementsystem_sde3;

public class TaskDemoSDE3 {
    public static void main(String[] args) {
        new NotificationService(); 

        BoardManager manager = new BoardManager();
        manager.loadTask(new Task("TASK_API_1"));

        System.out.println("Developer starts work...");
        manager.updateTask("TASK_API_1", TaskStatus.TODO, TaskStatus.IN_PROGRESS);
        
        System.out.println("Two developers try to submit PR drag-and-drop simultaneously...");
        manager.updateTask("TASK_API_1", TaskStatus.IN_PROGRESS, TaskStatus.IN_REVIEW);
        manager.updateTask("TASK_API_1", TaskStatus.IN_PROGRESS, TaskStatus.DONE); // Cas collision block expected
    }
}
