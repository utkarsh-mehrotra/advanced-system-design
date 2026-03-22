# 📈 Online Stock Brokerage System — SDE3 Redesign

This is a ground-up distributed systems redesign for an SDE3-level FinTech exchange architecture.

## How it works (The SDE3 Difference)
Unlike the previous monolithic, synchronous codebase, this architecture implements an **Asynchronous Event-Driven Pipeline** using the **LMAX Disruptor Pattern**.

1. **No Single JVM Lock!** The `OrderMatchingEngine` previously used a synchronized `ReentrantLock` that blocked all ingress. Now, the `MatchingEngineService` is completely lock-free. It uses a single Consumer Thread mapped to a specific symbol (e.g., AAPL). Since only one thread mutates the `PriorityQueue`, it avoids context switching and locks entirely—the secret to 1M+ TPS in production trading engines.
2. **True Service Boundaries:** The `LedgerService` controls account funds and portfolios entirely separated from the Matching Engine. They communicate exclusively via simulated MessageBus (Kafka).
3. **Optimistic Pre-Authorization:** Before an order enters the matching queue, the `OrderGatewayService` pre-authorizes the risk limits synchronously against the Ledger, but then hands the Order off asynchronously to the Matching Engine.
4. **Resiliency:** State changes (Order Created, Trade Executed) are pushed as Events. This forms an immutable Event Log, which guarantees state can be recovered via Event Sourcing.

## Submitting orders:

`Gateway` -> `(OrderCreatedEvent)` -> `Kafka` -> `Matching Engine` -> `(TradeExecutedEvent)` -> `Kafka` -> `Ledger / Database Settlement`

## Run the Demo
```bash
javac $(find . -name "*.java")
java onlinestockbrokeragesystem_sde3.SDE3StockBrokerageDemo
```
