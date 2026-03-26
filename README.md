# 🚀 Advanced System Design — SDE3-Level Low Level Design Implementations

A collection of **30+ Low Level Design (LLD) systems** in Java, each available in two versions:

- **`<system>/`** — Original SDE2 implementation (baseline)
- **`<system>_upgraded/`** — Refactored to SDE3/Staff Engineer standard

---

## 🎯 What Makes the Upgraded Versions SDE3-Level?

| Concern | SDE2 (Original) | SDE3 (Upgraded) |
|---------|----------------|-----------------|
| Concurrency | Global `synchronized` / Singleton locks | `ReentrantLock`, `ReadWriteLock`, `AtomicBoolean` CAS |
| Driver/Seat dispatch | `if (available) assign()` — TOCTOU race | `compareAndSet(true, false)` — CPU-level mutual exclusion |
| Financial math | `double` / `float` | `BigDecimal` with `RoundingMode.HALF_UP` |
| Notifications | Inline blocking calls | `ExecutorService` async fire-and-forget pipelines |
| State transitions | Unchecked `setStatus(anything)` | GoF State Pattern with illegal-transition guards |
| Search | Linear `for` loops | `parallelStream().filter()` using all CPU cores |
| Feed generation | O(N×M) pull-on-read | Fan-Out-On-Write → O(1) pre-computed feeds |
| Architecture | God-class Singletons | Dependency Injection + Facade pattern |

---

## 📦 Systems

### Batch 1 — Foundational Concurrency
| System | Original | Upgraded | Signature Upgrade |
|--------|---------|----------|-------------------|
| Parking Lot | `parkinglot/` | `parkinglot_upgraded/` | `Lock`-per-spot atomicity, Strategy-based fee calc |
| Splitwise | `splitwise/` | `splitwise_upgraded/` | Lexicographic lock ordering to eliminate deadlocks |
| Digital Wallet | `digitalwallet/` | `digitalwallet_upgraded/` | Two-account `ReentrantLock` ordering, `BigDecimal` balances |
| Airline Management | `airlinemanagementsystem/` | `airlinemanagementsystem_upgraded/` | Atomic seat reservation, `StripePaymentStrategy` |

### Batch 2 — State & Event-Driven Design
| System | Original | Upgraded | Signature Upgrade |
|--------|---------|----------|-------------------|
| Vending Machine | `vendingmachine/` | `vendingmachine_upgraded/` | GoF State Pattern (Idle → Ready → Dispense → ReturnChange) |
| Stack Overflow | `stackoverflow/` | `stackoverflow_upgraded/` | DDD, async reputation scoring via `VoteEvent` observers |
| Pub/Sub System | `pubsubsystem/` | `pubsubsystem_upgraded/` | `LinkedBlockingQueue`, per-Topic subscriber isolation |

### Batch 3 — Strategy & Temporal Logic
| System | Original | Upgraded | Signature Upgrade |
|--------|---------|----------|-------------------|
| Elevator System | `elevatorsystem/` | `elevatorsystem_upgraded/` | Min-Heap SCAN algorithm, `PriorityQueue` per direction |
| Hotel Management | `hotelmanagement/` | `hotelmanagement_upgraded/` | Interval bounding to prevent overlapping reservation races |
| LRU Cache | `lrucache/` | `lrucache_upgraded/` | Lock-striped segments for parallel shard access |

### Batch 4 — Distributed Transaction & Game Logic
| System | Original | Upgraded | Signature Upgrade |
|--------|---------|----------|-------------------|
| Library Management | `librarymanagementsystem/` | `librarymanagementsystem_upgraded/` | `AtomicBoolean` per-book micro-locking, `parallelStream()` catalog |
| Movie Ticket Booking | `movieticketbookingsystem/` | `movieticketbookingsystem_upgraded/` | Show-level `ReentrantLock`, `TEMPORARILY_HELD` seat state |
| ATM | `atm/` | `atm_upgraded/` | GoF State Machine, 2PC Saga compensating transactions |
| Restaurant Management | `restaurantmanagementsystem/` | `restaurantmanagementsystem_upgraded/` | Async kitchen `EventDispatcher`, tightest-fit table strategy |
| Chess Game | `chessgame/` | `chessgame_upgraded/` | Ray-casting collision engine, mathematical rollback checkmate |

### Batch 5 — Distributed Scale & Graph Systems
| System | Original | Upgraded | Signature Upgrade |
|--------|---------|----------|-------------------|
| Food Delivery | `fooddeliveryservice/` | `fooddeliveryservice_upgraded/` | CAS `AtomicBoolean` driver dispatch, async `NotificationDispatcher` |
| Social Networking | `socialnetworkingservice/` | `socialnetworkingservice_upgraded/` | Fan-Out-On-Write push feeds, `CopyOnWriteArrayList` for viral safety |
| Ride Sharing | `ridesharingservice/` | `ridesharingservice_upgraded/` | Per-Ride `ReentrantLock` + Driver CAS, `SurgePricingStrategy` |
| Stock Brokerage | `onlinestockbrokeragesystem/` | `onlinestockbrokeragesystem_upgraded/` | Dual PriorityQueue Order Book (Max-Bid / Min-Ask heap matching) |
| Task Management | `taskmanagementsystem/` | `taskmanagementsystem_upgraded/` | GoF State lifecycle guards, async `AuditObserver` history logging |

### Batch 6 — Advanced Concurrency & Sagas
| System | Original | Upgraded | Signature Upgrade |
|--------|---------|----------|-------------------|
| CricInfo | `cricinfo/` | `cricinfo_upgraded/` | `ConcurrentHashMap` + `AtomicInteger` lock-free scoring |
| Course Registration | `courseregistrationsystem/` | `courseregistrationsystem_upgraded/` | Disjoint target `ReentrantLock`s for capacity bounds |
| Music Streaming | `musicstreamingservice/` | `musicstreamingservice_upgraded/` | Asymmetric `ExecutorService` mimicking hardware playback |
| Online Shopping | `onlineshopping/` | `onlineshopping_upgraded/` | Transactional **Saga** pattern (inventory fallback) |
| Snake & Ladder | `snakeandladdergame/` | `snakeandladdergame_upgraded/` | O(1) graph navigation, `Callable<Player>` engines |

### Original-Only Systems (Not Yet Upgraded)
`carrentalsystem` · `coffeevendingmachine` · `concertbookingsystem` · `linkedin` · `onlineauctionsystem` · `tictactoe` · `trafficsignalsystem`

---

## 🏗️ Design Patterns Index

| Pattern | Where Applied |
|---------|--------------|
| **State** | ATM, Vending Machine, Task Management, Chess Game |
| **Strategy** | Payment (Airline, ATM), Pricing (Ride Sharing), Delivery Matching, Expense Splitting |
| **Observer / Event-Driven** | Restaurant Kitchen, Pub/Sub, Stack Overflow Reputation |
| **Repository / Facade** | All upgraded systems (replacing Singleton god-classes) |
| **Command** | Chess move history with rollback |
| **Two-Phase Commit Saga** | ATM withdrawal compensation |
| **Fan-Out-On-Write** | Social Networking newsfeed |
| **Order Book (Max/Min Heap)** | Stock Brokerage bid/ask matching |

---

## 🔧 Running a System

```bash
cd solutions/java/src

# Compile (e.g. Ride Sharing upgraded)
javac $(find ridesharingservice_upgraded -name "*.java")

# Run
java ridesharingservice_upgraded.RideSharingDemoUpgraded
```

Replace the package name with any `<system>_upgraded` or original `<system>` directory.

---

## 📋 Key Concurrency Concepts Demonstrated

- **TOCTOU Race Elimination** — `AtomicBoolean.compareAndSet()` prevents concurrent double-booking of drivers/seats
- **Lock Ordering** — Lexicographic acquisition order prevents circular deadlocks in multi-party transactions
- **Lock Striping** — Segment-level locks in LRU Cache allow parallel shard access
- **CopyOnWriteArrayList** — Lock-free snapshot iteration for viral social data
- **BlockingQueue Producer-Consumer** — Back-pressure-safe async pipelines in Pub/Sub
- **Volatile Visibility** — Cross-thread state reads without full locking overhead
