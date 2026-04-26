package atm_sde3.state;

import atm_sde3.Card;
import atm_sde3.AtmController;
import java.math.BigDecimal;

public interface ATMState {
    ATMState insertCard(AtmController context, Card card);
    ATMState enterPin(AtmController context, String pin);
    ATMState withdrawCash(AtmController context, BigDecimal amount);
    ATMState ejectCard(AtmController context);
}
