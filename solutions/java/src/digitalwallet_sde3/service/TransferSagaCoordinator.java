package digitalwallet_sde3.service;

import digitalwallet_sde3.database.SagaStore;
import digitalwallet_sde3.messaging.EventBus;
import digitalwallet_sde3.model.SagaStatus;
import digitalwallet_sde3.model.TransferSaga;
import digitalwallet_sde3.model.events.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * State Machine for cross-account distributed transactions.
 */
public class TransferSagaCoordinator {
    private final SagaStore sagaStore;
    private final AccountLedgerService ledgerService; // In reality, communicates via Kafka commands
    private final ExecutorService worker = Executors.newSingleThreadExecutor();

    public TransferSagaCoordinator(SagaStore sagaStore, AccountLedgerService ledgerService) {
        this.sagaStore = sagaStore;
        this.ledgerService = ledgerService;
        startEventLoop();
    }

    private void startEventLoop() {
        worker.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Event event = EventBus.getInstance().getSagaEventsQueue().take();
                    handleSagaEvent(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private void handleSagaEvent(Event event) {
        TransferSaga saga = sagaStore.getSaga(event.getSagaId());
        if (saga == null) return;

        if (event instanceof FundsReservedEvent) {
            System.out.println("[SAGA " + saga.getSagaId().substring(0,8) + "] Transition: PENDING -> RESERVED. Issuing Credit Command to " + saga.getToAccountId());
            saga.setStatus(SagaStatus.RESERVED);
            // Step 2: Send Credit Command to Ledger B
            ledgerService.executeCredit(saga.getToAccountId(), saga.getAmount(), saga.getSagaId());
            
        } else if (event instanceof FundsCreditedEvent) {
            System.out.println("[SAGA " + saga.getSagaId().substring(0,8) + "] Transition: RESERVED -> CREDITED. Issuing Commit Command to " + saga.getFromAccountId());
            saga.setStatus(SagaStatus.CREDITED);
            // Step 3: Send Commit Command to Ledger A
            ledgerService.executeCommit(saga.getFromAccountId(), saga.getAmount(), saga.getSagaId());
            saga.setStatus(SagaStatus.COMPLETED);
            System.out.println("[SAGA " + saga.getSagaId().substring(0,8) + "] Transfer COMPLETED successfully.");
            
        } else if (event instanceof FundsRejectedEvent) {
            FundsRejectedEvent rej = (FundsRejectedEvent) event;
            System.out.println("[SAGA " + saga.getSagaId().substring(0,8) + "] Transition: FAILED. Reason: " + rej.getReason());
            
            if (saga.getStatus() == SagaStatus.PENDING) {
                // Failed at step 1. Just mark failed.
                saga.setStatus(SagaStatus.FAILED_TO_RESERVE);
            } else if (saga.getStatus() == SagaStatus.RESERVED) {
                // Failed at step 2 (e.g., account B invalid). Rollback A.
                saga.setStatus(SagaStatus.ROLLBACK_INITIATED);
                System.out.println("[SAGA " + saga.getSagaId().substring(0,8) + "] Issuing Rollback Command to " + saga.getFromAccountId());
                ledgerService.executeRollback(saga.getFromAccountId(), saga.getAmount(), saga.getSagaId());
                saga.setStatus(SagaStatus.ROLLED_BACK);
            }
        }
    }

    public void shutdown() {
        worker.shutdownNow();
    }
}
