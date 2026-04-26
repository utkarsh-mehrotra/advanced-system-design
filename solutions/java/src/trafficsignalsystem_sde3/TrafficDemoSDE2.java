package trafficsignalsystem_sde3;

public class TrafficDemoSDE2 {
    public static void main(String[] args) {
        IntersectionController controller = new IntersectionController();
        controller.addLight(new TrafficLight("NORTH_SOUTH"));
        controller.addLight(new TrafficLight("EAST_WEST"));

        controller.transitionSignal("NORTH_SOUTH", SignalColor.GREEN);
        controller.transitionSignal("NORTH_SOUTH", SignalColor.YELLOW);
        controller.transitionSignal("NORTH_SOUTH", SignalColor.RED);
        
        controller.transitionSignal("EAST_WEST", SignalColor.GREEN);
    }
}
