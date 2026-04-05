package trafficsignalsystem_sde3;

public class TrafficDemoSDE3 {
    public static void main(String[] args) {
        IntersectionController controller = new IntersectionController();
        controller.addLight(new TrafficLight("NORTH_SOUTH"));
        controller.addLight(new TrafficLight("EAST_WEST"));

        EmergencyVehicleDetector sensor = new EmergencyVehicleDetector();
        System.out.println("Normal operation...");
        
        // Simulating highly concurrent emergency detection trigger
        sensor.detectAmbulance("EAST_WEST");
    }
}
