package linkedin_sde3;

public class ActivityLog {
    private final String userId;
    private final String action;
    private final String metadata;

    public ActivityLog(String userId, String action, String metadata) {
        this.userId = userId;
        this.action = action;
        this.metadata = metadata;
    }
    
    public String getAction() { return action; }
}
