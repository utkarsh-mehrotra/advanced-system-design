package airlinemanagementsystem_sde3;

public class BaggageService {
    public BaggageService() {
        EventBus.getInstance().subscribe("FLIGHT_STATE_CHANGED", this::handleFlightState);
    }

    private void handleFlightState(Object payload) {
        String data = (String) payload;
        if (data.contains("BOARDING")) {
            System.out.println("BaggageService [Async Check]: Triggering baggage loaded checks for " + data.split(":")[0]);
        }
    }
}
