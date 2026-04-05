package airlinemanagementsystem_sde3;

public class AirlineDemoSDE3 {
    public static void main(String[] args) {
        new BaggageService(); // Startup background services

        BookingManager manager = new BookingManager();
        manager.addFlight(new Flight("AI-101"));

        System.out.println("Beginning Flight State Transition...");
        manager.transitionFlightAsync("AI-101", FlightState.SCHEDULED, FlightState.BOARDING);
    }
}
