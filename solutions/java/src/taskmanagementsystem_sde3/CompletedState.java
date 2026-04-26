package taskmanagementsystem_sde3;

public class CompletedState implements TaskState {
    @Override
    public TaskState startProgress(Task context) {
        throw new IllegalStateException("Cannot restart a COMPLETED task. Reopen it first.");
    }

    @Override
    public TaskState complete(Task context) {
        throw new IllegalStateException("Task is already COMPLETED.");
    }

    @Override
    public TaskState reopen(Task context) {
        System.out.println("  [State] Task '" + context.getTitle() + "': COMPLETED → PENDING (reopened)");
        return new PendingState();
    }

    @Override
    public String getStatusName() { return "COMPLETED"; }
}
