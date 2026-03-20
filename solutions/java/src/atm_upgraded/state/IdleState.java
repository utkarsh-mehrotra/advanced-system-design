package atm_upgraded.state;

import atm_upgraded.ATM;
import atm_upgraded.Card;

import java.math.BigDecimal;

public class IdleState implements ATMState {

    @Override
    public void insertCard(ATM atm, Card card) {
        System.out.println("Card Inserted. Moving to Pin Entry...");
        atm.setInsertedCard(card);
        atm.setCurrentState(atm.getHasCardState());
    }

    @Override
    public void ejectCard(ATM atm) {
        System.out.println("No card to eject.");
    }

    @Override
    public void enterPin(ATM atm, String pin) {
        System.out.println("Please insert a card first.");
    }

    @Override
    public void withdrawCash(ATM atm, BigDecimal amount) {
        System.out.println("Please insert a card first.");
    }

    @Override
    public void depositCash(ATM atm, BigDecimal amount) {
        System.out.println("Please insert a card first.");
    }

    @Override
    public void checkBalance(ATM atm) {
        System.out.println("Please insert a card first.");
    }
}
