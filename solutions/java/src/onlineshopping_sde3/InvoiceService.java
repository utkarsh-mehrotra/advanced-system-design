package onlineshopping_sde3;

public class InvoiceService {
    public InvoiceService() {
        EventBus.getInstance().subscribe("ORDER_CONFIRMED", this::dispatchInvoice);
    }

    private void dispatchInvoice(Object payload) {
        System.out.println("InvoiceService [Async Task]: Connecting to Payment Gateway for -> " + payload);
    }
}
