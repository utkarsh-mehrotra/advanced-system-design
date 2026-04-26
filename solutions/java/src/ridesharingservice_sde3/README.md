# 🚗 Ride Sharing Service — SDE3 Upgraded

## Overview
An Uber-style ride-hailing system with passenger ride requests, proximity-based driver notification, atomic ride acceptance, and pluggable surge pricing. Eliminates the core multi-driver double-acceptance race condition via a two-layer locking protocol.

## SDE3 Upgrades Applied

| Issue | Fix |
|-------|-----|
| `if (ride.getStatus() == REQUESTED) ride.setDriver(...)` — multiple driver threads pass and all get assigned | Per-`Ride` `ReentrantLock` + Driver `AtomicBoolean.compareAndSet()` — exactly one thread succeeds |
| `Math.random()` distance approximation | Haversine formula on real lat/lon coordinates |
| Hardcoded fare = `distance * flatRate` | `PricingStrategy` interface → `DefaultPricingStrategy`, `SurgePricingStrategy` — injectable |
| O(N) distance sweep on the request thread | `RideDispatcher` with `ExecutorService` broadcasts to nearby drivers asynchronously |

## Class Diagram

```mermaid
classDiagram
    class RideSharingFacade {
        -List~Driver~ drivers
        -PricingStrategy pricingStrategy
        -RideDispatcher dispatcher
        +requestRide(passengerId, source, destination) Ride
        +acceptRide(rideId, driverId) boolean
        +startRide(rideId)
        +completeRide(rideId) BigDecimal
    }
    class Ride {
        -RideStatus status
        -Driver driver
        -ReentrantLock rideLock
        +assignDriverAtomically(Driver) boolean
        +startRide() boolean
        +completeRide(BigDecimal) boolean
    }
    class Driver {
        -Location location
        -AtomicBoolean available
        +claimForRide() boolean
        +releaseFromRide()
    }
    class PricingStrategy {
        <<interface>>
        +calculateFare(distanceKm, durationMin) BigDecimal
    }
    class DefaultPricingStrategy
    class SurgePricingStrategy {
        -double surgeMultiplier
    }
    class RideDispatcher {
        -ExecutorService pool
        +broadcastToNearbyDrivers(Ride, List~Driver~)
    }
    class Location {
        +distanceTo(Location) double
    }

    RideSharingFacade --> RideDispatcher
    RideSharingFacade --> PricingStrategy
    RideSharingFacade "1" *-- "many" Driver
    PricingStrategy <|.. DefaultPricingStrategy
    PricingStrategy <|.. SurgePricingStrategy
    Ride --> Driver
    Driver --> Location
```

## Run
```bash
javac $(find ridesharingservice_upgraded -name "*.java")
java ridesharingservice_upgraded.RideSharingDemoUpgraded
```
