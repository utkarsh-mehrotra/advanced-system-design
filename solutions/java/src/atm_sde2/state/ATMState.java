package atm_sde2.state;

import atm_sde2.ATM;
import atm_sde2.Card;

import java.math.BigDecimal;

public interface ATMState {
    void insertCard(ATM atm, Card card);
    void ejectCard(ATM atm);
    void enterPin(ATM atm, String pin);
    void withdrawCash(ATM atm, BigDecimal amount);
    void depositCash(ATM atm, BigDecimal amount);
    void checkBalance(ATM atm);
}
