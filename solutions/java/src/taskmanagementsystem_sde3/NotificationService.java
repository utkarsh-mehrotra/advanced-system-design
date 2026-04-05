package taskmanagementsystem_sde3;

public class NotificationService {
    public NotificationService() {
        EventBus.getInstance().subscribe("TASK_STATUS_CHANGED", this::fireBurndownMetrics);
    }

    private void fireBurndownMetrics(Object payload) {
        System.out.println("NotificationService [Async Analytics]: Updating Sprint Burndown Chart with -> " + payload);
    }
}
