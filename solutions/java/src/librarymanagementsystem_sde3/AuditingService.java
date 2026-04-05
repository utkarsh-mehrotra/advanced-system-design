package librarymanagementsystem_sde3;

public class AuditingService {
    public AuditingService() {
        EventBus.getInstance().subscribe("BOOK_BORROWED", this::logAudit);
    }

    private void logAudit(Object payload) {
        System.out.println("AuditingService [Async Write]: Setting due-date and emailing payload -> " + payload);
    }
}
