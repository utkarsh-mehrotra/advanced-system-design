# 💳 Digital Wallet — SDE3 Upgraded

## Overview
A peer-to-peer digital wallet modelling UPI/PayPal-style fund transfers. Eliminates deadlocks in concurrent two-party transfers using strict lock ordering, and uses BigDecimal for financial precision.

## SDE3 Upgrades Applied

| Issue | Fix |
|-------|-----|
| Risk of deadlock when User A→B and User B→A transfer simultaneously | Lexicographic wallet ID ordering enforces a deterministic lock acquisition sequence |
| `double` balance fields | `BigDecimal` with `HALF_UP` rounding |
| Single global transfer lock — all transfers serialize | Per-Wallet `ReentrantLock`; only involved wallets are locked |

## Class Diagram

```mermaid
classDiagram
    class DigitalWalletFacade {
        -Map~String,Wallet~ wallets
        +createWallet(User) Wallet
        +transfer(fromId, toId, BigDecimal)
        +deposit(walletId, BigDecimal)
        +withdraw(walletId, BigDecimal)
    }
    class Wallet {
        -String id
        -BigDecimal balance
        -ReentrantLock lock
        +debit(BigDecimal) boolean
        +credit(BigDecimal)
        +getBalance() BigDecimal
    }
    class Transaction {
        -String id
        -String fromWalletId
        -String toWalletId
        -BigDecimal amount
        -TransactionStatus status
        -Instant timestamp
    }
    class PaymentStrategy {
        <<interface>>
        +pay(Wallet from, Wallet to, BigDecimal)
    }
    class User {
        -String id
        -String name
    }

    DigitalWalletFacade "1" *-- "many" Wallet
    DigitalWalletFacade --> PaymentStrategy
    Wallet --> Transaction
    Wallet -- User
```

## Run
```bash
javac $(find digitalwallet_upgraded -name "*.java")
java digitalwallet_upgraded.DigitalWalletDemoUpgraded
```
