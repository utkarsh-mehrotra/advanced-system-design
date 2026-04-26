package atm_sde3;

import atm_sde3.state.ATMState;
import atm_sde3.state.IdleState;
import atm_sde3.state.HasPinState;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class AtmController {
    private final AtomicReference<ATMState> activeState = new AtomicReference<>(new IdleState());
    
    // Thread-safe context variables (volatile since they are just references)
    private volatile Card insertedCard;

    public void setCard(Card card) {
        this.insertedCard = card;
    }

    public Card getCard() {
        return insertedCard;
    }

    public void insertCard(Card card) {
        while (true) {
            ATMState current = activeState.get();
            ATMState next = current.insertCard(this, card);
            if (activeState.compareAndSet(current, next)) {
                break;
            }
        }
    }

    public void enterPin(String pin) {
        while (true) {
            ATMState current = activeState.get();
            ATMState next = current.enterPin(this, pin);
            if (activeState.compareAndSet(current, next)) {
                break;
            }
        }
    }

    public void processWithdrawal(Account account, BigDecimal amount) {
        while (true) {
            ATMState current = activeState.get();
            if (!(current instanceof HasPinState)) {
                System.out.println("AtmController: Action denied. PIN must be verified first.");
                return;
            }

            // Execute transition
            ATMState next = current.withdrawCash(this, amount);
            
            if (activeState.compareAndSet(current, next)) {
                // SDE3 Async logic triggered after successful lock-free transition
                System.out.println("AtmController: Initiating withdrawal sequence for $" + amount);
                if (account.withdraw(amount.doubleValue())) {
                    System.out.println("AtmController: Ledger debited successfully.");
                    
                    // Decoupled Event Pipeline (SDE3 element)
                    EventBus.getInstance().publish("DISPENSE_CASH", amount);
                    EventBus.getInstance().publish("AUDIT_LOG", "Withdrawal:" + account.getAccountNumber() + ":" + amount);
                } else {
                    System.out.println("AtmController: Verification failed (Insufficient Funds).");
                }
                
                // End by ejecting card and resetting state implicitly via the State return
                this.setCard(null);
                break;
            }
        }
    }
}

