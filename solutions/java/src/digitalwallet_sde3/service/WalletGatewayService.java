package digitalwallet_sde3.service;

import digitalwallet_sde3.database.SagaStore;
import digitalwallet_sde3.messaging.EventBus;
import digitalwallet_sde3.model.TransferSaga;
import digitalwallet_sde3.model.events.TransferRequestedEvent;

import java.math.BigDecimal;

/**
 * Public Web API equivalent.
 */
public class WalletGatewayService {
    private final SagaStore sagaStore;

    public WalletGatewayService(SagaStore sagaStore) {
        this.sagaStore = sagaStore;
    }

    public String initiateTransfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        // Create the Saga State
        TransferSaga saga = new TransferSaga(fromAccountId, toAccountId, amount);
        sagaStore.save(saga);

        System.out.println("[GATEWAY] Initiating Transfer Saga " + saga.getSagaId().substring(0,8) + ": $" + amount + " from " + fromAccountId + " -> " + toAccountId);

        // Fire the initial event to the bus (Ledger picks this up)
        EventBus.getInstance().publishToLedger(new TransferRequestedEvent(saga.getSagaId(), fromAccountId, toAccountId, amount));

        // Return immediately (HTTP 202 Accepted)
        return saga.getSagaId();
    }
}
