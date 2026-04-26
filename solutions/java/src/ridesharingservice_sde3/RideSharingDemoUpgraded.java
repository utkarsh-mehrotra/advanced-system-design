package ridesharingservice_sde3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RideSharingDemoUpgraded {
    public static void main(String[] args) throws InterruptedException {

        // --- Demo 1: Normal flow with Surge Pricing ---
        System.out.println("=== DEMO 1: Surge Pricing Strategy ===");
        PricingStrategy surgeStrategy = new SurgePricingStrategy(2.5);
        RideSharingFacade surge = new RideSharingFacade(surgeStrategy);

        Passenger alice = new Passenger("P1", "Alice", "555-2001");
        Driver mike = new Driver("D1", "Mike", "AB-1234", new Location(12.9716, 77.5946));
        surge.addPassenger(alice);
        surge.addDriver(mike);

        Ride surgeRide = surge.requestRide("P1", new Location(12.9716, 77.5946), new Location(13.0827, 80.2707));
        Thread.sleep(100);
        surge.acceptRide(surgeRide.getId(), "D1");
        surge.startRide(surgeRide.getId());
        surge.completeRide(surgeRide.getId());
        surge.shutdown();

        System.out.println();

        // --- Demo 2: Concurrent Multi-Driver Accept Race ---
        System.out.println("=== DEMO 2: Concurrent Multi-Driver Accept (CAS Race Proof) ===");
        PricingStrategy defaultPricing = new DefaultPricingStrategy();
        RideSharingFacade rideshare = new RideSharingFacade(defaultPricing);

        Passenger bob = new Passenger("P2", "Bob", "555-2002");
        Driver d1 = new Driver("D1", "Driver Alpha", "CD-5678", new Location(12.97, 77.59));
        Driver d2 = new Driver("D2", "Driver Beta", "EF-9012", new Location(12.98, 77.60));
        Driver d3 = new Driver("D3", "Driver Gamma", "GH-3456", new Location(12.96, 77.58));

        rideshare.addPassenger(bob);
        rideshare.addDriver(d1);
        rideshare.addDriver(d2);
        rideshare.addDriver(d3);

        Ride contested = rideshare.requestRide("P2",
                new Location(12.979, 77.591),
                new Location(12.990, 77.610));
        Thread.sleep(100);

        System.out.println("Simulating 3 drivers pressing ACCEPT simultaneously...");
        CountDownLatch startGate = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(3);

        pool.submit(() -> { try { startGate.await(); rideshare.acceptRide(contested.getId(), "D1"); } catch (Exception ignored) {} });
        pool.submit(() -> { try { startGate.await(); rideshare.acceptRide(contested.getId(), "D2"); } catch (Exception ignored) {} });
        pool.submit(() -> { try { startGate.await(); rideshare.acceptRide(contested.getId(), "D3"); } catch (Exception ignored) {} });

        startGate.countDown(); // Fire all 3 simultaneously
        pool.shutdown();
        Thread.sleep(500);

        System.out.println("\nResult: Ride assigned to -> "
                + (contested.getDriver() != null ? contested.getDriver().getName() : "NO ONE (BUG!)"));
        System.out.println("Ride status: " + contested.getStatus());

        rideshare.shutdown();
    }
}
