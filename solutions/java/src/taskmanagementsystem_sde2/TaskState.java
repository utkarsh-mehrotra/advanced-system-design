package taskmanagementsystem_sde2;

/**
 * SDE3: GoF State Pattern interface.
 * Concrete states enforce valid lifecycle transitions, throwing IllegalStateException for invalid ones.
 */
public interface TaskState {
    TaskState startProgress(Task context);
    TaskState complete(Task context);
    TaskState reopen(Task context);
    String getStatusName();
}
