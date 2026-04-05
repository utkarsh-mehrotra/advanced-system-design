package parkinglot_sde3;

public class EntrancePanel {
    public EntrancePanel() {
        EventBus.getInstance().subscribe("TICKET_ISSUED", this::dispenseTicket);
    }

    private void dispenseTicket(Object payload) {
        System.out.println("EntrancePanel [Async Hardware]: Rolling out ticket for Spot " + payload);
    }
}
