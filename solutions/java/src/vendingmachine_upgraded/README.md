# 🥤 Vending Machine — SDE3 Upgraded

## Overview
A vending machine modelling cash payment, inventory tracking, and change dispensing. Implements the GoF State Pattern to make illegal transitions (e.g., dispensing before payment) structurally impossible.

## SDE3 Upgrades Applied

| Issue | Fix |
|-------|-----|
| `if/else` chains on a status enum — illegal states reachable | GoF State Pattern: `IdleState`, `ReadyState`, `DispenseState`, `ReturnChangeState` |
| `int` quantity in Inventory — concurrent restock races | `ConcurrentHashMap` + `AtomicInteger` quantities |
| `double` price comparisons | `BigDecimal` for precise cash/change arithmetic |

## Class Diagram

```mermaid
classDiagram
    class VendingMachine {
        -VendingMachineState currentState
        -Inventory inventory
        -BigDecimal insertedAmount
        +insertMoney(BigDecimal)
        +selectProduct(String)
        +dispense()
        +returnChange()
        +setState(VendingMachineState)
    }
    class VendingMachineState {
        <<interface>>
        +insertMoney(VendingMachine, BigDecimal)
        +selectProduct(VendingMachine, String)
        +dispense(VendingMachine)
        +returnChange(VendingMachine)
    }
    class IdleState
    class ReadyState
    class DispenseState
    class ReturnChangeState
    class Inventory {
        -ConcurrentHashMap~String,AtomicInteger~ stock
        +addProduct(Product, int)
        +tryDecrementStock(String) boolean
    }
    class Product {
        -String id
        -String name
        -BigDecimal price
    }

    VendingMachine --> VendingMachineState
    VendingMachine --> Inventory
    Inventory "1" *-- "many" Product
    VendingMachineState <|.. IdleState
    VendingMachineState <|.. ReadyState
    VendingMachineState <|.. DispenseState
    VendingMachineState <|.. ReturnChangeState
```

## Run
```bash
javac $(find vendingmachine_upgraded -name "*.java")
java vendingmachine_upgraded.VendingMachineDemoUpgraded
```
