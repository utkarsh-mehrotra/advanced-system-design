package concertbookingsystem_sde3;

public class InvoiceService {
    public InvoiceService() {
        EventBus.getInstance().subscribe("GENERATE_INVOICE", this::generateInvoiceAsync);
    }

    private void generateInvoiceAsync(Object payload) {
        String data = (String) payload;
        System.out.println("InvoiceService [Async Worker]: Generating PDF Invoice for -> " + data);
        // Save to DB, upload to S3, send email, etc.
    }
}
