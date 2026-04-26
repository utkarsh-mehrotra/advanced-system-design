package restaurantmanagementsystem_sde3;

import java.util.Comparator;
import java.util.List;

public class TableAllocationStrategy {

    /**
     * SDE3: Topological Search Strategy.
     * Finds the absolute tightest-fit Table that can physically hold the party.
     * I.e If party is 3 people, it selects a 4-top instead of wasting an 8-top.
     */
    public Table allocateOptimalTable(List<Table> allTables, int partySize) {
        // We utilize Java Streams to filter AVAILABLE tables capable of holding the party,
        // then we MIN-sort them by Capacity distance to find the most efficient routing.
        return allTables.stream()
                .filter(t -> t.getStatus() == TableStatus.AVAILABLE && t.getCapacity() >= partySize)
                .min(Comparator.comparingInt(Table::getCapacity))
                .orElse(null);
    }
}
