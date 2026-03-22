# 💳 Digital Wallet — SDE3 Redesign (Distributed Saga Architecture)

The prior "upgraded" implementation avoided deadlocks by acquiring two simultaneous JVM `ReentrantLock`s sorted lexicographically by wallet ID. 

While this avoids deadlocks in a monolithic single-JVM application, it **violates Distributed Systems principles**. You physically cannot wrap a `ReentrantLock` around a network call to update two distributed partitioned databases (e.g., Alice's DynamoDB shard and Bob's DynamoDB shard). Doing so via traditional Two-Phase Commit (2PC) destroys system throughput and availability.

## The SDE3 Redesign
This implementation uses the **Saga Pattern** with asynchronous **Event-Driven Messaging** (simulated Kafka).

### Architecture
1. **WalletGatewayService:** Handles the HTTP API. It creates a `TransferSaga(PENDING)` explicitly modeling the Distributed Transaction state machine, fires an event, and returns HTTP 202 Accepted.
2. **AccountLedgerService:** Handles the actual DB state. **Crucially, it never locks two accounts at once.** It evaluates `Alice`'s balance entirely locally.
3. **TransferSagaCoordinator:** The orchestrator. It listens to completion events from the Ledgers and directs the next step of the Saga.

### Transaction Flow
1. `Gateway` -> creates Saga state.
2. `Ledger` -> Reserves Funds from Alice -> emits `FundsReservedEvent`.
3. `Coordinator` hears event -> Issues `CreditCommand` for Bob.
4. `Ledger` -> Credits Bob -> emits `FundsCreditedEvent`.
5. `Coordinator` hears event -> Issues `CommitCommand` for Alice. 
   - *(The reserved money is permanently subtracted).*

If Bob's account is frozen or invalid, the `Coordinator` fires a `RollbackCommand` to Alice to refund her.

Because accounts are evaluated individually in series across a message bus, **Deadlocks are mathematically impossible** and throughput can scale horizontally indefinitely.

## Run the Demo
```bash
javac $(find . -name "*.java")
java digitalwallet_sde3.SDE3DigitalWalletDemo
```
