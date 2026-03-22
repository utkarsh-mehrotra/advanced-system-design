package parkinglot_sde2;

import parkinglot_sde2.exception.ParkingLotFullException;
import parkinglot_sde2.strategy.HourlyPricingStrategy;
import parkinglot_sde2.strategy.NearestSpotStrategy;
import parkinglot_sde2.vehicletype.Car;
import parkinglot_sde2.vehicletype.Motorcycle;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParkingLotDemoUpgraded {
    public static void main(String[] args) {
        // Dependency Injection
        NearestSpotStrategy strategy = new NearestSpotStrategy();
        HourlyPricingStrategy pricing = new HourlyPricingStrategy(new BigDecimal("5.00")); // $5/hour
        
        ParkingLotFacade lot = new ParkingLotFacade(strategy, pricing);

        // Bootstrap a small lot: 1 floor with 5 spots
        // Using exactly 5 spots, meaning ratio 50:40:10 yields: 2 bikes, 2 cars, 1 truck
        lot.addLevel(new Level(1, 5));

        System.out.println("--- SDE3 Parking Lot Demo Started ---");
        lot.displayAvailability();

        ParkingTicket ticket1 = null;
        try {
            // Vehicle Entry (Car 1)
            Car car1 = new Car("ABC-123");
            ticket1 = lot.entryGate(car1);

            // Vehicle Entry (Car 2)
            Car car2 = new Car("XYZ-987");
            ParkingTicket ticket2 = lot.entryGate(car2);

            // Vehicle Entry (Car 3) -> Should Fail, only 2 car spots exist
            System.out.println("\n--- Expecting Lot Full Exception for Car 3 ---");
            Car car3 = new Car("DEF-456");
            lot.entryGate(car3); // throws exception

        } catch (ParkingLotFullException e) {
            System.err.println("Caught Expected Error: " + e.getMessage());
        }

        lot.displayAvailability();

        System.out.println("\n--- Processing Exits ---");
        
        try {
            if (ticket1 != null) {
                String tId = ticket1.getTicketId();
                BigDecimal cost = lot.exitGate(tId);
                System.out.println("Final Charge for Car 1: $" + cost);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        lot.displayAvailability();
    }
}
