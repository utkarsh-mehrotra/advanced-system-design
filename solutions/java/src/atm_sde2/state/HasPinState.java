package atm_sde2.state;

import atm_sde2.ATM;
import atm_sde2.Card;

import java.math.BigDecimal;

public class HasPinState implements ATMState {

    @Override
    public void insertCard(ATM atm, Card card) {
        System.out.println("Card is already inserted.");
    }

    @Override
    public void ejectCard(ATM atm) {
        System.out.println("Card Ejected. Ending Session.");
        atm.setInsertedCard(null);
        atm.setCurrentState(atm.getIdleState());
    }

    @Override
    public void enterPin(ATM atm, String pin) {
        System.out.println("PIN already authenticated.");
    }

    @Override
    public void withdrawCash(ATM atm, BigDecimal amount) {
        atm.executeWithdrawalTransaction(amount);
        ejectCard(atm);
    }

    @Override
    public void depositCash(ATM atm, BigDecimal amount) {
        atm.executeDepositTransaction(amount);
        ejectCard(atm);
    }

    @Override
    public void checkBalance(ATM atm) {
        atm.executeCheckBalance();
        ejectCard(atm);
    }
}
