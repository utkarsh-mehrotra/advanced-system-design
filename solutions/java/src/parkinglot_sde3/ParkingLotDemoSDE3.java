package parkinglot_sde3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParkingLotDemoSDE3 {
    public static void main(String[] args) throws InterruptedException {
        new EntrancePanel(); 
        new BillingService();

        ParkingLotManager manager = new ParkingLotManager(500);
        manager.registerSpot(new ParkingSpot("L1_P1"));

        ExecutorService executor = Executors.newFixedThreadPool(4);
        for(int i = 0; i < 4; i++) {
            executor.submit(() -> {
                String spot = manager.attemptAssignment();
                if (spot == null) {
                    System.out.println("Driver rejected natively. Full lot.");
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
    }
}
