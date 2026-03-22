package digitalwallet_sde3.messaging;

import digitalwallet_sde3.model.events.Event;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * MessageBroker (Kafka / RabbitMQ Simulation).
 * Distributes events between the Saga Coordinator and the Ledger Services.
 */
public class EventBus {
    private static final EventBus INSTANCE = new EventBus();
    
    // Ledger listens here
    private final BlockingQueue<Event> ledgerCommands = new LinkedBlockingQueue<>();
    
    // Saga Coordinator listens here
    private final BlockingQueue<Event> sagaEvents = new LinkedBlockingQueue<>();

    private EventBus() {}

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public void publishToLedger(Event event) {
        ledgerCommands.offer(event);
    }

    public void publishToSagaCoordinator(Event event) {
        sagaEvents.offer(event);
    }

    public BlockingQueue<Event> getLedgerCommandsQueue() {
        return ledgerCommands;
    }

    public BlockingQueue<Event> getSagaEventsQueue() {
        return sagaEvents;
    }
}
