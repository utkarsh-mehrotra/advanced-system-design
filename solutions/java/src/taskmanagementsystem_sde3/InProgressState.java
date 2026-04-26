package taskmanagementsystem_sde3;

public class InProgressState implements TaskState {
    @Override
    public TaskState startProgress(Task context) {
        throw new IllegalStateException("Task is already IN_PROGRESS.");
    }

    @Override
    public TaskState complete(Task context) {
        System.out.println("  [State] Task '" + context.getTitle() + "': IN_PROGRESS → COMPLETED");
        return new CompletedState();
    }

    @Override
    public TaskState reopen(Task context) {
        throw new IllegalStateException("Cannot reopen an IN_PROGRESS task.");
    }

    @Override
    public String getStatusName() { return "IN_PROGRESS"; }
}
