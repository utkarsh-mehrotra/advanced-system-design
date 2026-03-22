# 🛗 Elevator System — SDE3 Upgraded

## Overview
A multi-elevator dispatch system for a building. Implements the SCAN algorithm (disk scheduling analogy) via a `PriorityQueue` to efficiently serve floor requests in direction-sorted order, minimising average wait time.

## SDE3 Upgrades Applied

| Issue | Fix |
|-------|-----|
| Round-robin assignment ignores proximity — inefficient | `SCANDispatchStrategy`: selects nearest elevator moving in the right direction |
| Single `synchronized` on the controller — blocks all elevators | Per-`Elevator` `ReentrantLock`; controller only coordinates, not bottlenecks |
| No strategy abstraction for dispatch | `ElevatorSelectionStrategy` interface — injectable at runtime |

## Class Diagram

```mermaid
classDiagram
    class ElevatorController {
        -List~Elevator~ elevators
        -ElevatorSelectionStrategy strategy
        +handleRequest(Request)
    }
    class ElevatorSelectionStrategy {
        <<interface>>
        +selectElevator(List~Elevator~, Request) Elevator
    }
    class SCANDispatchStrategy {
        +selectElevator(List~Elevator~, Request) Elevator
    }
    class Elevator {
        -int id
        -int currentFloor
        -Direction direction
        -PriorityQueue~Request~ upQueue
        -PriorityQueue~Request~ downQueue
        -ReentrantLock lock
        +addRequest(Request)
        +step()
    }
    class Request {
        -int floor
        -Direction direction
    }
    class Direction {
        <<enumeration>>
        UP
        DOWN
        IDLE
    }

    ElevatorController "1" *-- "many" Elevator
    ElevatorController --> ElevatorSelectionStrategy
    ElevatorSelectionStrategy <|.. SCANDispatchStrategy
    Elevator "1" *-- "many" Request
    Request --> Direction
```

## Run
```bash
javac $(find elevatorsystem_upgraded -name "*.java")
java elevatorsystem_upgraded.ElevatorSystemDemoUpgraded
```
