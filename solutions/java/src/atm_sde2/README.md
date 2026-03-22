# 🏧 ATM — SDE3 Upgraded

## Overview
An ATM system modelling card insertion, PIN validation, balance enquiry, withdrawal, and deposit. Implements the GoF State Pattern for hardware lifecycle management and a Two-Phase Commit Saga to coordinate bank ledger updates with cash dispenser atomically.

## SDE3 Upgrades Applied

| Issue | Fix |
|-------|-----|
| `if/else` status checks — illegal operations reachable (withdraw before PIN entry) | GoF State Machine: `IdleState → HasCardState → HasPinState → SelectionState` |
| `double` account balance — IEEE 754 cents errors | `BigDecimal` with `HALF_UP` rounding for all monetary values |
| Bank debit + cash dispense not coordinated — partial failures possible | Two-Phase Commit Saga with compensating transaction (re-credit) if dispenser fails |

## Class Diagram

```mermaid
classDiagram
    class ATM {
        -ATMState currentState
        -CashDispenser cashDispenser
        -BankingService bankingService
        -Card insertedCard
        +insertCard(Card)
        +enterPin(int)
        +withdraw(BigDecimal)
        +deposit(BigDecimal)
        +ejectCard()
        +setState(ATMState)
    }
    class ATMState {
        <<interface>>
        +insertCard(ATM, Card)
        +enterPin(ATM, int)
        +withdraw(ATM, BigDecimal)
    }
    class IdleState
    class HasCardState
    class HasPinState
    class CashDispenser {
        -AtomicInteger cashAvailable
        +dispenseCash(BigDecimal) boolean
    }
    class BankingService {
        +debit(Account, BigDecimal) boolean
        +credit(Account, BigDecimal)
        +getBalance(Account) BigDecimal
    }
    class Transaction {
        -TransactionType type
        -BigDecimal amount
        -Instant timestamp
    }

    ATM --> ATMState
    ATM --> CashDispenser
    ATM --> BankingService
    ATMState <|.. IdleState
    ATMState <|.. HasCardState
    ATMState <|.. HasPinState
    BankingService --> Transaction
```

## Run
```bash
javac $(find atm_upgraded -name "*.java")
java atm_upgraded.ATMDemoUpgraded
```
