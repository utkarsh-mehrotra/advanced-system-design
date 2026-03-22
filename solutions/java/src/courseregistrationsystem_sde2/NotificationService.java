package courseregistrationsystem_sde2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationService {
    private final ExecutorService asyncPool;

    public NotificationService() {
        this.asyncPool = Executors.newFixedThreadPool(2);
    }

    public void notifyStudentOfRegistration(Student student, Course course) {
        asyncPool.submit(() -> {
            System.out.println("[ASYNC EVENT] Email sent to " + student.getEmail() 
                + ": Successfully registered for " + course.getName());
        });
    }

    public void notifyStudentOfFailure(Student student, Course course) {
        asyncPool.submit(() -> {
            System.out.println("[ASYNC EVENT] Email sent to " + student.getEmail() 
                + ": Registration failed for " + course.getName() + " (Course Full)");
        });
    }

    public void shutdown() {
        asyncPool.shutdown();
    }
}
