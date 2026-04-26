package atm_sde3.state;

import atm_sde3.Card;
import atm_sde3.AtmController;
import java.math.BigDecimal;

public class HasPinState implements ATMState {
    @Override
    public ATMState insertCard(AtmController context, Card card) {
        System.out.println("Card already inserted.");
        return this;
    }

    @Override
    public ATMState enterPin(AtmController context, String pin) {
        System.out.println("PIN already validated.");
        return this;
    }

    @Override
    public ATMState withdrawCash(AtmController context, BigDecimal amount) {
        // Validation occurs in the Controller logic before emitting Dispense Action
        return new IdleState();
    }

    @Override
    public ATMState ejectCard(AtmController context) {
        context.setCard(null);
        System.out.println("Card ejected.");
        return new IdleState();
    }
}
