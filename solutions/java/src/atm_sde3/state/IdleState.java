package atm_sde3.state;

import atm_sde3.Card;
import atm_sde3.AtmController;
import java.math.BigDecimal;

public class IdleState implements ATMState {
    @Override
    public ATMState insertCard(AtmController context, Card card) {
        context.setCard(card);
        return new HasCardState();
    }

    @Override
    public ATMState enterPin(AtmController context, String pin) {
        System.out.println("No card inserted.");
        return this;
    }

    @Override
    public ATMState withdrawCash(AtmController context, BigDecimal amount) {
        System.out.println("No card inserted.");
        return this;
    }

    @Override
    public ATMState ejectCard(AtmController context) {
        System.out.println("No card inserted.");
        return this;
    }
}
