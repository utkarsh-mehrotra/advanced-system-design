# 🚀 LLD Upgraded — SDE3-Level Low Level Design Implementations

A collection of **20 production-grade Low Level Design (LLD) systems** in Java, refactored from SDE2 to **SDE3/Staff Engineer standard**. Each system addresses real-world concurrency pitfalls, applies canonical GoF design patterns, and models authentic industry-scale architectures.

---

## 🎯 What Makes These SDE3-Level?

| Concern | SDE2 Approach | SDE3 Upgrade Applied Here |
|---------|--------------|---------------------------|
| Concurrency | Global `synchronized` locks | `ReentrantLock`, `ReadWriteLock`, `AtomicBoolean` CAS |
| Driver/Seat dispatch | `if (available) { assign() }` TOCTOU race | `compareAndSet(true, false)` CPU-level mutual exclusion |
| Financial math | `double` / `float` | `BigDecimal` with `RoundingMode.HALF_UP` |
| Notifications | Inline blocking calls | `ExecutorService` async fire-and-forget pipelines |
| State management | `setStatus(anything)` | GoF State Pattern with illegal transition guards |
| Search | Linear `for` loops | `parallelStream().filter()` using all CPU cores |
| Feed generation | O(N×M) pull-on-read | Fan-Out-On-Write → O(1) pre-computed feeds |
| Singletons | Global god-class monitor | Dependency Injection + Facade pattern |

---

## 📦 Systems Implemented

### Batch 1 — Foundational Concurrency
| System | Package | Signature Upgrade |
|--------|---------|-------------------|
| Parking Lot | `parkinglot_upgraded` | `Lock`-per-spot atomicity, Strategy-based fee calculation |
| Splitwise | `splitwise_upgraded` | Lexicographic lock ordering to eliminate deadlocks |
| Digital Wallet | `digitalwallet_upgraded` | Two-account `ReentrantLock` ordering, `BigDecimal` balances |
| Airline Management | `airlinemanagementsystem_upgraded` | Atomic seat reservation, Stripe `PaymentStrategy` |

### Batch 2 — State & Event-Driven Design
| System | Package | Signature Upgrade |
|--------|---------|-------------------|
| Vending Machine | `vendingmachine_upgraded` | GoF State Pattern (Idle → Ready → Dispense → ReturnChange) |
| Stack Overflow | `stackoverflow_upgraded` | DDD, async reputation scoring via `VoteEvent` observers |
| Pub/Sub System | `pubsubsystem_upgraded` | Blocking `LinkedBlockingQueue`, per-Topic subscriber isolation |

### Batch 3 — Strategy & Temporal Logic
| System | Package | Signature Upgrade |
|--------|---------|-------------------|
| Elevator System | `elevatorsystem_upgraded` | Min-Heap SCAN algorithm dispatch, `PriorityQueue` per direction |
| Hotel Management | `hotelmanagement_upgraded` | Interval bounding to prevent overlapping reservation races |
| LRU Cache | `lrucache_upgraded` | Lock-striped segments for parallel shard access |

### Batch 4 — Distributed Transaction & Game Logic
| System | Package | Signature Upgrade |
|--------|---------|-------------------|
| Library Management | `librarymanagementsystem_upgraded` | `AtomicBoolean` per-book micro-locking, `parallelStream()` catalog |
| Movie Ticket Booking | `movieticketbookingsystem_upgraded` | Show-level `ReentrantLock`, `TEMPORARILY_HELD` seat state |
| ATM | `atm_upgraded` | GoF State (Idle→HasCard→HasPin), 2PC Saga compensating transactions |
| Restaurant Management | `restaurantmanagementsystem_upgraded` | Async kitchen `EventDispatcher`, tightest-fit `TableAllocationStrategy` |
| Chess Game | `chessgame_upgraded` | Ray-casting collision engine, Mathematical Rollback checkmate detection |

### Batch 5 — Distributed Scale & Graph Systems
| System | Package | Signature Upgrade |
|--------|---------|-------------------|
| Food Delivery | `fooddeliveryservice_upgraded` | CAS `AtomicBoolean` driver dispatch, async `NotificationDispatcher` |
| Social Networking | `socialnetworkingservice_upgraded` | Fan-Out-On-Write push feeds, `CopyOnWriteArrayList` for viral safety |
| Ride Sharing | `ridesharingservice_upgraded` | Per-Ride `ReentrantLock` + Driver CAS, `SurgePricingStrategy` |
| Stock Brokerage | `onlinestockbrokeragesystem_upgraded` | Dual PriorityQueue Order Book (Max-Bid / Min-Ask heap matching) |
| Task Management | `taskmanagementsystem_upgraded` | GoF State lifecycle guards, async `AuditObserver` history logging |

---

## 🏗️ Design Patterns Index

| Pattern | Where Applied |
|---------|--------------|
| **State** | ATM, Vending Machine, Task Management, Chess Game |
| **Strategy** | Payment (Airline, ATM), Pricing (Ride Sharing), Delivery Matching, Expense Splitting |
| **Observer / Event-Driven** | Restaurant Kitchen, Pub/Sub, Stack Overflow Reputation |
| **Repository / Facade** | All systems (replacing Singleton god-classes) |
| **Command** | Chess move history with rollback |
| **Two-Phase Commit Saga** | ATM withdrawal compensation |
| **Fan-Out-On-Write** | Social Networking newsfeed |
| **Order Book (Max/Min Heap)** | Stock Brokerage bid/ask matching |

---

## 🔧 Running a System

All implementations are plain Java with no external dependencies.

```bash
cd solutions/java/src

# Compile a system (e.g., Ride Sharing)
javac $(find ridesharingservice_upgraded -name "*.java")

# Run the demo
java ridesharingservice_upgraded.RideSharingDemoUpgraded
```

Replace `ridesharingservice_upgraded` with any `*_upgraded` package name.

---

## 📋 Key Concurrency Concepts Demonstrated

- **TOCTOU Race Elimination** — `AtomicBoolean.compareAndSet()` prevents concurrent double-booking of drivers/seats
- **Lock Ordering** — Lexicographic acquisition order prevents circular deadlocks in multi-party transactions
- **Lock Striping** — Segment-level locks in LRU Cache allow parallel shard access
- **Volatile Visibility** — `volatile` fields ensure cross-thread state reads without full locking
- **CopyOnWriteArrayList** — Lock-free reads with snapshot-consistent iteration for viral social data
- **BlockingQueue Producer-Consumer** — Back-pressure-safe async pipelines in Pub/Sub

---

## 📂 Repository Structure

```
solutions/java/src/
├── airlinemanagementsystem_upgraded/
├── atm_upgraded/
├── chessgame_upgraded/
├── digitalwallet_upgraded/
├── elevatorsystem_upgraded/
├── fooddeliveryservice_upgraded/
├── hotelmanagement_upgraded/
├── librarymanagementsystem_upgraded/
├── lrucache_upgraded/
├── movieticketbookingsystem_upgraded/
├── onlinestockbrokeragesystem_upgraded/
├── parkinglot_upgraded/
├── pubsubsystem_upgraded/
├── restaurantmanagementsystem_upgraded/
├── ridesharingservice_upgraded/
├── socialnetworkingservice_upgraded/
├── splitwise_upgraded/
├── stackoverflow_upgraded/
├── taskmanagementsystem_upgraded/
└── vendingmachine_upgraded/
```
