package stackoverflow_sde3;

public class ReputationService {
    public ReputationService() {
        EventBus.getInstance().subscribe("UPVOTE_RECEIVED", this::computePoints);
    }

    private void computePoints(Object payload) {
        System.out.println("ReputationService [Async]: Adding +10 Points to Profile -> " + payload);
    }
}
