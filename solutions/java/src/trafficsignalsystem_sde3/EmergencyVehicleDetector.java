package trafficsignalsystem_sde3;

public class EmergencyVehicleDetector {
    public void detectAmbulance(String axisId) {
        System.out.println("V2X Sensor: Ambulance detected approaching axis " + axisId);
        EventBus.getInstance().publish("EMERGENCY_VEHICLE_DETECTED", axisId);
    }
}
