package movieticketbookingsystem_sde3;

public class TicketService {
    public TicketService() {
        EventBus.getInstance().subscribe("TICKET_CONFIRMED", this::issueDigitalTicket);
    }

    private void issueDigitalTicket(Object payload) {
        System.out.println("TicketService [Async Worker]: Emitting QR Code payload -> " + payload);
    }
}
