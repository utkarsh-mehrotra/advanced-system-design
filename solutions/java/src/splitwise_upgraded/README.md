# 💸 Splitwise — SDE3 Upgraded

## Overview
A multi-party expense splitting system supporting equal, exact, and percentage splits. Models Splitwise's core debt settlement engine with deadlock-safe concurrent balance updates.

## SDE3 Upgrades Applied

| Issue | Fix |
|-------|-----|
| Unguarded balance modifications — concurrent updates corrupt totals | Lexicographic `ReentrantLock` ordering on two-user pairs eliminates circular deadlocks |
| Hardcoded equal split only | `ExpenseSplitter` Strategy interface with `EqualExpenseSplitter`, `ExactExpenseSplitter`, `PercentExpenseSplitter` |
| Raw `double` balances | `BigDecimal` with `RoundingMode.HALF_UP` throughout |

## Class Diagram

```mermaid
classDiagram
    class SplitwiseService {
        -Map~String,User~ users
        -BalanceManager balanceManager
        +addExpense(Expense, ExpenseSplitter)
        +settleDebt(String payerId, String payeeId, BigDecimal)
    }
    class BalanceManager {
        -ConcurrentHashMap balances
        -ReentrantLock lockA, lockB
        +debit(userId, amount)
        +credit(userId, amount)
        +getBalance(userId) BigDecimal
    }
    class Expense {
        -String id
        -User paidBy
        -BigDecimal amount
        -List~User~ participants
    }
    class ExpenseSplitter {
        <<interface>>
        +split(Expense) Map~User,BigDecimal~
    }
    class EqualExpenseSplitter
    class ExactExpenseSplitter
    class PercentExpenseSplitter
    class Split {
        -User user
        -BigDecimal amount
    }

    SplitwiseService --> BalanceManager
    SplitwiseService --> ExpenseSplitter
    ExpenseSplitter <|.. EqualExpenseSplitter
    ExpenseSplitter <|.. ExactExpenseSplitter
    ExpenseSplitter <|.. PercentExpenseSplitter
    Expense "1" *-- "many" Split
```

## Run
```bash
javac $(find splitwise_upgraded -name "*.java")
java splitwise_upgraded.SplitwiseDemoUpgraded
```
