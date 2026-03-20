package taskmanagementsystem_upgraded;

public class PendingState implements TaskState {
    @Override
    public TaskState startProgress(Task context) {
        System.out.println("  [State] Task '" + context.getTitle() + "': PENDING → IN_PROGRESS");
        return new InProgressState();
    }

    @Override
    public TaskState complete(Task context) {
        throw new IllegalStateException("Cannot complete a PENDING task — start it first.");
    }

    @Override
    public TaskState reopen(Task context) {
        throw new IllegalStateException("Task is already PENDING.");
    }

    @Override
    public String getStatusName() { return "PENDING"; }
}
