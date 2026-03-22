package taskmanagementsystem_sde2;

import java.time.LocalDate;
import java.util.List;

public class TaskManagementDemoUpgraded {
    public static void main(String[] args) throws InterruptedException {
        TaskManagementFacade jira = new TaskManagementFacade();

        User alice = jira.registerUser("Alice");
        User bob   = jira.registerUser("Bob");

        System.out.println("=== Task Lifecycle via GoF State Machine ===");
        Task t1 = jira.createTask("Implement login API", "OAuth2 integration", 1, LocalDate.now().plusDays(7), alice);
        Task t2 = jira.createTask("Write unit tests", "Cover login flows", 2, LocalDate.now().plusDays(10), bob);

        jira.startTask(t1.getId(), alice);
        jira.completeTask(t1.getId(), alice);

        System.out.println("\n--- Attempting illegal transition (COMPLETED → IN_PROGRESS) ---");
        try {
            jira.startTask(t1.getId(), alice);
        } catch (IllegalStateException e) {
            System.out.println("  Correctly blocked: " + e.getMessage());
        }

        System.out.println("\n--- Reopen and re-complete (regression scenario) ---");
        jira.reopenTask(t1.getId(), bob);
        jira.startTask(t1.getId(), alice);
        jira.completeTask(t1.getId(), alice);

        System.out.println("\n=== Parallel Stream Search ===");
        Task t3 = jira.createTask("Implement OAuth2 callback", "Handle redirect URI flows", 1, LocalDate.now().plusDays(5), alice);
        List<Task> results = jira.searchTasks("OAuth2");
        System.out.println("Tasks matching 'OAuth2': " + results.size());
        results.forEach(t -> System.out.println("  - " + t.getTitle() + " [" + t.getStatus() + "]"));

        // Give async audit log time to flush
        Thread.sleep(200);

        System.out.println("\n=== Async Audit Log (" + jira.getAuditLog().size() + " entries) ===");
        jira.getAuditLog().forEach(System.out::println);

        jira.shutdown();
    }
}
