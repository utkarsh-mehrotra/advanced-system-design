package atm_upgraded.state;

import atm_upgraded.ATM;
import atm_upgraded.Card;

import java.math.BigDecimal;

public interface ATMState {
    void insertCard(ATM atm, Card card);
    void ejectCard(ATM atm);
    void enterPin(ATM atm, String pin);
    void withdrawCash(ATM atm, BigDecimal amount);
    void depositCash(ATM atm, BigDecimal amount);
    void checkBalance(ATM atm);
}
