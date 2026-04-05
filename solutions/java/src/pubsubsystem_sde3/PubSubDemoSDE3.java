package pubsubsystem_sde3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PubSubDemoSDE3 {
    public static void main(String[] args) throws InterruptedException {
        new Logger(); // Active listener hook
        
        PubSubController controller = new PubSubController();
        controller.registerSubscriber("SYSTEM_ALERTS", new Subscriber("SUB_1"));
        controller.registerSubscriber("SYSTEM_ALERTS", new Subscriber("SUB_2"));

        System.out.println("Unleashing concurrent publish barrage...");
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(() -> controller.fireEvent("SYSTEM_ALERTS", "CPU_SPIKE"));
        executor.submit(() -> controller.fireEvent("SYSTEM_ALERTS", "DISK_FULL"));
        executor.submit(() -> controller.fireEvent("SYSTEM_ALERTS", "NET_DROP"));

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
    }
}
