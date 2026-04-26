package fooddeliveryservice_sde3;

import fooddeliveryservice_sde3.order.Order;
import java.util.List;

public class RandomOptimalAgentStrategy implements DeliveryMatchingStrategy {

    @Override
    public DeliveryAgent findAndLockOptimalAgent(List<DeliveryAgent> allAgents, Order order) {
        // SDE3: Real Uber/DoorDash systems use geospatial indexing here.
        // For LLD simulation, we iterate. The CRITICAL difference is the CAS Lock!
        for (DeliveryAgent agent : allAgents) {
            
            // We ONLY read the volatile state as a hint to avoid hitting locks unnecessarily
            if (agent.isAvailable()) {
                
                // THE ATOMIC MUTUAL EXCLUSION BINDING
                // If two threads hit this exact line simultaneously, only ONE evaluates to true!
                if (agent.lockAssignment()) {
                    System.out.println("Strategy: Exclusively locked driver " + agent.getName() + " for Order " + order.getId());
                    return agent;
                } else {
                    System.out.println("Strategy: Missed driver " + agent.getName() + " due to parallel contention. Searching next...");
                }
            }
        }
        return null;
    }
}
