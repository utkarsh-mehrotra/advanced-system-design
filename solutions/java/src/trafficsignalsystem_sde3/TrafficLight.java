package trafficsignalsystem_sde3;

public class TrafficLight {
    private final String id;
    private SignalColor currentColor;

    public TrafficLight(String id) {
        this.id = id;
        this.currentColor = SignalColor.RED; // Default state
    }

    public String getId() { return id; }
    public SignalColor getCurrentColor() { return currentColor; }
    
    // Package-private, controller manages mutation
    void setCurrentColor(SignalColor color) {
        this.currentColor = color;
    }
}
