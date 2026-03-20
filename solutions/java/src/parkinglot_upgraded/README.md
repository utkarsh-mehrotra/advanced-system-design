# 🅿️ Parking Lot — SDE3 Upgraded

## Overview
A thread-safe, multi-level parking lot system handling concurrent vehicle entry/exit without overbooking spots. Models real-world parking infrastructure with pluggable fee calculation.

## SDE3 Upgrades Applied

| Issue | Fix |
|-------|-----|
| Global synchronized lock — all vehicles serialize on one monitor | Per-spot `ReentrantLock` allowing parallel occupancy on different spots |
| Hardcoded flat fee | `FeeCalculationStrategy` interface — swap `HourlyFeeStrategy`, `FlatFeeStrategy` at runtime |
| Primitive `boolean available` on ParkingSpot | `AtomicBoolean` with `tryLock()` for contention-free CAS claim |

## Class Diagram

```mermaid
classDiagram
    class ParkingLot {
        -List~Level~ levels
        +parkVehicle(Vehicle) ParkingSpot
        +unparkVehicle(ParkingSpot)
    }
    class Level {
        -int floorNumber
        -List~ParkingSpot~ spots
        +parkVehicle(Vehicle) ParkingSpot
    }
    class ParkingSpot {
        -VehicleType allowedType
        -AtomicBoolean isOccupied
        -ReentrantLock lock
        +tryOccupy(Vehicle) boolean
        +vacate()
    }
    class FeeCalculationStrategy {
        <<interface>>
        +calculateFee(Duration) BigDecimal
    }
    class HourlyFeeStrategy {
        +calculateFee(Duration) BigDecimal
    }
    class Vehicle {
        <<abstract>>
        -String licensePlate
        -VehicleType type
    }
    class Car
    class Motorcycle
    class Truck

    ParkingLot "1" *-- "many" Level
    Level "1" *-- "many" ParkingSpot
    ParkingSpot ..> Vehicle
    ParkingLot --> FeeCalculationStrategy
    FeeCalculationStrategy <|.. HourlyFeeStrategy
    Vehicle <|-- Car
    Vehicle <|-- Motorcycle
    Vehicle <|-- Truck
```

## Run
```bash
javac $(find parkinglot_upgraded -name "*.java")
java parkinglot_upgraded.ParkingLotDemoUpgraded
```
