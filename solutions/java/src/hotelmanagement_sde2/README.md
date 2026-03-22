# 🏨 Hotel Management System — SDE3 Upgraded

## Overview
A hotel room reservation platform handling concurrent bookings, check-in/check-out, and multi-method payment. The SDE3 upgrade introduces interval bounding to prevent overlapping reservations — the core correctness bug in the original.

## SDE3 Upgrades Applied

| Issue | Fix |
|-------|-----|
| Two guests can book the same room for overlapping dates simultaneously | Per-Room `ReentrantLock` + date-overlap check inside critical section |
| `double` for room pricing | `BigDecimal` with `RoundingMode.HALF_UP` |
| Single payment method hardcoded | `Payment` interface → `CreditCardPayment`, `CashPayment` strategies |

## Class Diagram

```mermaid
classDiagram
    class Hotel {
        -List~Room~ rooms
        +bookRoom(Guest, RoomType, LocalDate, LocalDate, Payment) Reservation
        +checkIn(reservationId)
        +checkOut(reservationId)
        +cancelReservation(reservationId)
    }
    class Room {
        -String number
        -RoomType type
        -BigDecimal pricePerNight
        -List~Reservation~ reservations
        -ReentrantLock lock
        +tryBook(checkIn, checkOut) boolean
    }
    class Reservation {
        -String id
        -Guest guest
        -Room room
        -LocalDate checkIn
        -LocalDate checkOut
        -ReservationStatus status
    }
    class Guest {
        -String id
        -String name
        -String contact
    }
    class Payment {
        <<interface>>
        +processPayment(BigDecimal) boolean
    }
    class CreditCardPayment
    class CashPayment
    class RoomType {
        <<enumeration>>
        SINGLE
        DOUBLE
        SUITE
    }

    Hotel "1" *-- "many" Room
    Room "1" *-- "many" Reservation
    Reservation --> Guest
    Reservation --> Payment
    Payment <|.. CreditCardPayment
    Payment <|.. CashPayment
    Room --> RoomType
```

## Run
```bash
javac $(find hotelmanagement_upgraded -name "*.java")
java hotelmanagement_upgraded.HotelManagementSystemDemoUpgraded
```
