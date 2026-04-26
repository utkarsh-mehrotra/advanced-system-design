package atm_sde3.state;

import atm_sde3.Card;
import atm_sde3.AtmController;
import java.math.BigDecimal;

public class HasCardState implements ATMState {
    @Override
    public ATMState insertCard(AtmController context, Card card) {
        System.out.println("Card already inserted.");
        return this;
    }

    @Override
    public ATMState enterPin(AtmController context, String pin) {
        if (context.getCard().getPin().equals(pin)) {
            System.out.println("PIN Accepted.");
            return new HasPinState();
        } else {
            System.out.println("Invalid PIN.");
            context.setCard(null);
            return new IdleState();
        }
    }

    @Override
    public ATMState withdrawCash(AtmController context, BigDecimal amount) {
        System.out.println("Enter PIN first.");
        return this;
    }

    @Override
    public ATMState ejectCard(AtmController context) {
        context.setCard(null);
        System.out.println("Card ejected.");
        return new IdleState();
    }
}
