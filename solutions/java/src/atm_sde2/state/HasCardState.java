package atm_sde2.state;

import atm_sde2.ATM;
import atm_sde2.Card;

import java.math.BigDecimal;

public class HasCardState implements ATMState {

    @Override
    public void insertCard(ATM atm, Card card) {
        System.out.println("Card already inserted.");
    }

    @Override
    public void ejectCard(ATM atm) {
        System.out.println("Card Ejected.");
        atm.setInsertedCard(null);
        atm.setCurrentState(atm.getIdleState());
    }

    @Override
    public void enterPin(ATM atm, String pin) {
        if (atm.getInsertedCard().getPin().equals(pin)) {
            System.out.println("PIN Authenticated.");
            atm.setCurrentState(atm.getHasPinState());
        } else {
            System.out.println("Invalid PIN. Ejecting Card.");
            ejectCard(atm);
        }
    }

    @Override
    public void withdrawCash(ATM atm, BigDecimal amount) {
        System.out.println("Please enter PIN first.");
    }

    @Override
    public void depositCash(ATM atm, BigDecimal amount) {
       System.out.println("Please enter PIN first.");
    }

    @Override
    public void checkBalance(ATM atm) {
       System.out.println("Please enter PIN first.");
    }
}
