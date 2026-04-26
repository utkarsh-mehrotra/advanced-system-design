package elevatorsystem_sde3;

import elevatorsystem_sde3.strategy.SCANDispatchStrategy;

public class ElevatorSystemDemoUpgraded {
    public static void main(String[] args) throws InterruptedException {
        // SDE3: Inject our advanced SCAN Dispatch algorithm into the controller!
        ElevatorController controller = new ElevatorController(3, 5, new SCANDispatchStrategy());

        System.out.println("--- Starting SDE3 Elevator Simulation (SCAN Logic) ---");

        // The elevator should efficiently sweep.
        // User 1 going UP from 2 to 10
        controller.requestElevator(2, 10);
        
        Thread.sleep(50); // Small processing gap
        // User 2 going UP from 4 to 12. 
        // Elevator 1 should intercept this perfectly gracefully on its way to 10!
        controller.requestElevator(4, 12);
        
        Thread.sleep(50);
        // User 3 going DOWN from 8 to 1.
        // Elevator 2 should be cleanly dispatched to intercept this downstream!
        controller.requestElevator(8, 1);

        // Let system stabilize and print threads
        Thread.sleep(4000);
        System.out.println("--- Demo Completed ---");
        System.exit(0);
    }
}
