package atm_sde2;

import atm_sde2.state.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public class ATM {
    private final BankingService bankingService;
    private final CashDispenser cashDispenser;

    // GoF Internal States
    private final ATMState idleState;
    private final ATMState hasCardState;
    private final ATMState hasPinState;

    private ATMState currentState;
    private Card insertedCard;

    private static final AtomicLong transactionCounter = new AtomicLong(0);

    public ATM(BankingService bankingService, CashDispenser cashDispenser) {
        this.bankingService = bankingService;
        this.cashDispenser = cashDispenser;

        this.idleState = new IdleState();
        this.hasCardState = new HasCardState();
        this.hasPinState = new HasPinState();

        this.currentState = idleState; // Origin point
    }

    // --- Core State Transition APIs ---

    public void insertCard(Card card) { currentState.insertCard(this, card); }
    public void enterPin(String pin) { currentState.enterPin(this, pin); }
    public void withdrawCash(BigDecimal amount) { currentState.withdrawCash(this, amount); }
    public void ejectCard() { currentState.ejectCard(this); }

    // --- State Accessors (Package-Private logic utilized by States) ---

    public void setCurrentState(ATMState state) { this.currentState = state; }
    public void setInsertedCard(Card card) { this.insertedCard = card; }
    public Card getInsertedCard() { return insertedCard; }

    public ATMState getIdleState() { return idleState; }
    public ATMState getHasCardState() { return hasCardState; }
    public ATMState getHasPinState() { return hasPinState; }

    // --- Inner Transaction Mechanics (The SAGA Layer) ---

    public void executeWithdrawalTransaction(BigDecimal amount) {
        String accountNum = insertedCard.getLinkedAccountNumber();
        Account account = bankingService.getAccount(accountNum);

        if (account == null) {
            System.out.println("Fatal: Bank Account unreachable.");
            return;
        }

        // Fast fail: Does the ATM even possess this cash?
        if (!cashDispenser.canDispense(amount.intValue())) {
            System.out.println("This ATM lacks sufficient physical bills ($" + amount + ") to honor this request.");
            return;
        }

        String transactionId = generateTransactionId();

        // ----------------------------------------------------
        // PHASE 1 SAGA: Central Bank Debit
        // ----------------------------------------------------
        try {
            bankingService.processTransaction(new WithdrawalTransaction(transactionId, account, amount));
            System.out.println("Bank Server Accepted: Account debited $" + amount + ". Proceeding safely to hardware drop...");
        } catch (IllegalArgumentException e) {
            System.out.println("Bank Server Denied: " + e.getMessage());
            return;
        }

        // ------------------------------------------------------------------------------------------------
        // PHASE 2 SAGA: Mechanical Dispersion & COMPENSATING ROLLBACK 
        // ------------------------------------------------------------------------------------------------
        try {
            // Can inherently fail randomly if mechanical jams hit
            cashDispenser.dispenseCash(amount.intValue());
        } catch (RuntimeException hardwareFailure) {
            System.err.println("CRITICAL FAULT DETECTED: " + hardwareFailure.getMessage());
            System.out.println("[SAGA INTERVENTION]: Initiating high-priority Compacting Refund to Bank Server...");
            
            String rollbackTxId = "ROLLBACK-" + transactionId;
            bankingService.processTransaction(new DepositTransaction(rollbackTxId, account, amount));
            
            System.out.println("[SAGA INTERVENTION]: Funds successfully restored via 2PC Compensating Transaction.");
        }
    }

    public void executeDepositTransaction(BigDecimal amount) {
        Account account = bankingService.getAccount(insertedCard.getLinkedAccountNumber());
        bankingService.processTransaction(new DepositTransaction(generateTransactionId(), account, amount));
        System.out.println("Deposited $" + amount + " smoothly into Account.");
    }

    public void executeCheckBalance() {
        Account account = bankingService.getAccount(insertedCard.getLinkedAccountNumber());
        System.out.println("Current Balance: $" + account.getBalance());
    }

    private String generateTransactionId() {
        long txNumber = transactionCounter.incrementAndGet();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMyyHHmmss"));
        return "TXN-" + timestamp + "-" + String.format("%04d", txNumber);
    }
}
