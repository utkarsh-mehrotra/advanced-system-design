package pubsubsystem_sde3;

public class PubSubSystemDemoUpgraded {
    public static void main(String[] args) throws InterruptedException {
        MessageBroker broker = new MessageBroker();

        // Admin registers topics
        broker.createTopic("SoftwareEngineering");
        broker.createTopic("StockMarket");

        Topic softwareTopic = broker.getTopic("SoftwareEngineering");
        Topic stockTopic = broker.getTopic("StockMarket");

        // Create Subscribers
        // We simulate a SLOW consumer taking 2000ms. In the original LLD, 
        // this would freeze the Publisher. Here it merely occupies a background thread.
        PrintSubscriber slowSubscriber = new PrintSubscriber("S1_Slow", 2000); 
        PrintSubscriber fastSubscriber = new PrintSubscriber("S2_Fast", 100);

        broker.subscribe("SoftwareEngineering", slowSubscriber);
        broker.subscribe("SoftwareEngineering", fastSubscriber);
        broker.subscribe("StockMarket", fastSubscriber);

        Publisher publisher = new Publisher(broker);

        System.out.println("\n--- Initiating Async Event Burst ---");
        long start = System.currentTimeMillis();

        // Publish burst
        publisher.publish(softwareTopic, new Message("Java 21 Released!"));
        publisher.publish(stockTopic, new Message("NASDAQ hits new high"));
        publisher.publish(softwareTopic, new Message("SDE3 Patterns are crucial"));

        long end = System.currentTimeMillis();
        System.out.println("Producer finished publishing 3 messages in: " + (end - start) + "ms! (True Fire-and-Forget)");
        System.out.println("Notice how the Producer thread completely finished BEFORE the slow consumers printed!\n");

        // Wait to allow the background threads to process the 2000ms delay tasks
        Thread.sleep(3000);
        
        broker.shutdown();
    }
}
