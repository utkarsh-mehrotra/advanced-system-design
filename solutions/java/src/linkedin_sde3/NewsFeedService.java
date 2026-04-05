package linkedin_sde3;

public class NewsFeedService {
    public NewsFeedService() {
        // Asynchronously listens to new interactions that might affect Feeds
        EventBus.getInstance().subscribe("USER_POST_CREATED", this::processNewPost);
    }

    private void processNewPost(Object payload) {
        ActivityLog log = (ActivityLog) payload;
        System.out.println("NewsFeedService [Async]: Fan-out write process triggered for " + log.getAction());
        // Simulates fan-out cache update for connections' timelines
    }
}
