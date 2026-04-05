package restaurantmanagementsystem_sde3;

import java.util.concurrent.atomic.AtomicReference;

public class Table {
    private final String tableId;
    private final AtomicReference<TableStatus> status;

    public Table(String tableId) {
        this.tableId = tableId;
        this.status = new AtomicReference<>(TableStatus.AVAILABLE);
    }

    public String getTableId() { return tableId; }

    public boolean reserve() {
        return status.compareAndSet(TableStatus.AVAILABLE, TableStatus.RESERVED);
    }

    public boolean markOccupied() {
        return status.compareAndSet(TableStatus.RESERVED, TableStatus.OCCUPIED) 
            || status.compareAndSet(TableStatus.AVAILABLE, TableStatus.OCCUPIED);
    }
}
