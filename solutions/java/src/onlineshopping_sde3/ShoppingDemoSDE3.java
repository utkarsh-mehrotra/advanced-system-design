package onlineshopping_sde3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ShoppingDemoSDE3 {
    public static void main(String[] args) throws InterruptedException {
        new InvoiceService();

        ShoppingCartManager manager = new ShoppingCartManager();
        manager.addCatalogItem(new InventoryItem("IPHONE_15", 999.0, 2));

        // Simultaneous flash sale buying
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(() -> manager.checkout("U1", "IPHONE_15", 1));
        executor.submit(() -> manager.checkout("U2", "IPHONE_15", 1));
        executor.submit(() -> manager.checkout("U3", "IPHONE_15", 1)); // This should fail natively lock-free

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
    }
}
