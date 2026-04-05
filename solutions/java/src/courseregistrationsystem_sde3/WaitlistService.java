package courseregistrationsystem_sde3;

public class WaitlistService {
    public WaitlistService() {
        EventBus.getInstance().subscribe("COURSE_FULL_WAITLIST", this::handleWaitlist);
    }

    private void handleWaitlist(Object payload) {
        System.out.println("WaitlistService [Async Worker]: Persisting waitlist queue for -> " + payload);
    }
}
