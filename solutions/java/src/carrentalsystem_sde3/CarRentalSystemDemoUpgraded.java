package carrentalsystem_sde3;

import java.util.Optional;

public class CarRentalSystemDemoUpgraded {
    public static void main(String[] args) {
        RentalSystem system = new RentalSystem(new StandardPricingStrategy());
        system.addCar(new Car("XYZ-123", "Toyota Camry"));
        
        Customer customer = new Customer("C1", "Alice");
        
        Optional<Reservation> res = system.bookCar(customer, "XYZ-123", 3);
        res.ifPresent(r -> System.out.println("Booked! ID: " + r.getReservationId() + " Cost: " + r.getTotalCost()));
        
        // Attempt double booking
        Customer c2 = new Customer("C2", "Bob");
        Optional<Reservation> res2 = system.bookCar(c2, "XYZ-123", 2);
        System.out.println("Second booking successful? " + res2.isPresent());

        // Return
        if (res.isPresent()) {
            system.returnCar(res.get().getReservationId());
            System.out.println("Car returned.");
        }
    }
}
