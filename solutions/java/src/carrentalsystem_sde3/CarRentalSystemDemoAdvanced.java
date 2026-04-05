package carrentalsystem_sde3;

import java.util.Optional;

public class CarRentalSystemDemoAdvanced {
    public static void main(String[] args) {
        // Advanced decoupling: A standalone PaymentProcessor mock acting on events
        EventBus.getInstance().subscribe("BOOKING_INITIATED", event -> {
            Reservation res = (Reservation) event;
            System.out.println("PaymentProcessor: Processing payment for " + res.getId());
            // Simulating successful payment...
            EventBus.getInstance().publish("PAYMENT_SUCCESS", res.getId());
        });

        RentalSystem system = new RentalSystem();
        system.addCar(new Car("SDE3-100", "Tesla Model 3"));

        Optional<Reservation> res = system.initiateBooking("Cust_Advanced", "SDE3-100");
        res.ifPresent(r -> {
            System.out.println("Main: Booking Initiated. Current State: " + r.getState());
            // The async handler would have flipped it to CONFIRMED
        });
    }
}
