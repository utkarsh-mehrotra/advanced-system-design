package digitalwallet_sde3.database;

import digitalwallet_sde3.model.TransferSaga;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simulates the Event Store / State Database for active and completed Sagas.
 */
public class SagaStore {
    private final ConcurrentHashMap<String, TransferSaga> sagas = new ConcurrentHashMap<>();

    public void save(TransferSaga saga) {
        sagas.put(saga.getSagaId(), saga);
    }

    public TransferSaga getSaga(String sagaId) {
        return sagas.get(sagaId);
    }
}
