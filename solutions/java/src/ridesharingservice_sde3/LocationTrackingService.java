package ridesharingservice_sde3;

public class LocationTrackingService {
    public LocationTrackingService() {
        EventBus.getInstance().subscribe("RIDE_STATE_CHANGE", this::updateMaps);
    }

    private void updateMaps(Object payload) {
        System.out.println("LocationTrackingService [Async Worker]: Rendering new state to map GUI -> " + payload);
    }
}
