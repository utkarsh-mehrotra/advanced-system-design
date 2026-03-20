package atm_upgraded;

public class Card {
    private final String cardNumber;
    private final String pin;
    private final String linkedAccountNumber;

    public Card(String cardNumber, String pin, String linkedAccountNumber) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.linkedAccountNumber = linkedAccountNumber;
    }

    public String getCardNumber() { return cardNumber; }
    public String getPin() { return pin; }
    public String getLinkedAccountNumber() { return linkedAccountNumber; }
}
