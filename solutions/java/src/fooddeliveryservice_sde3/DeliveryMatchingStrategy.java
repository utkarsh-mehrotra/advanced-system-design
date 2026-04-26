package fooddeliveryservice_sde3;

import fooddeliveryservice_sde3.order.Order;
import java.util.List;

public interface DeliveryMatchingStrategy {
    /**
     * SDE3: Abstracted searching algorithm.
     * Guaranteed to process lockAssignment() cleanly or bounce to next agent.
     */
    DeliveryAgent findAndLockOptimalAgent(List<DeliveryAgent> allAgents, Order order);
}
