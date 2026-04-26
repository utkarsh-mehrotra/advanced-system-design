# 🎬 Movie Ticket Booking System — SDE3 Upgraded

## Overview
A cinema seat reservation system handling concurrent bookings across multiple shows. Introduces a `TEMPORARILY_HELD` intermediate seat state and per-show locking to eliminate the classic double-booking race.

## SDE3 Upgrades Applied

| Issue | Fix |
|-------|-----|
| Global lock — all shows serialize on one monitor | Per-`Show` `ReentrantLock`; parallel bookings on different shows contend only on the same show |
| AVAILABLE → BOOKED in two steps — TOCTOU window | Atomic AVAILABLE → TEMPORARILY_HELD → BOOKED three-phase commit inside lock |
| `SeatStatus` mutated without holding any lock | `synchronized seat.tryHold()` and `seat.confirm()` |

## Class Diagram

```mermaid
classDiagram
    class BookingFacade {
        -Map~String,Show~ shows
        +bookSeats(userId, showId, seatIds) Booking
        +cancelBooking(bookingId)
    }
    class Show {
        -String id
        -Movie movie
        -Theater theater
        -Map~String,Seat~ seats
        -ReentrantLock showLock
        +tryHoldSeats(seatIds) boolean
        +confirmSeats(seatIds)
        +releaseSeats(seatIds)
    }
    class Seat {
        -String id
        -SeatType type
        -SeatStatus status
        +tryHold() boolean
        +confirm()
        +release()
    }
    class SeatStatus {
        <<enumeration>>
        AVAILABLE
        TEMPORARILY_HELD
        BOOKED
    }
    class Booking {
        -String id
        -String userId
        -List~Seat~ seats
        -BookingStatus status
    }
    class Movie {
        -String id
        -String title
    }

    BookingFacade "1" *-- "many" Show
    Show "1" *-- "many" Seat
    Seat --> SeatStatus
    BookingFacade "1" *-- "many" Booking
    Show --> Movie
```

## Run
```bash
javac $(find movieticketbookingsystem_upgraded -name "*.java")
java movieticketbookingsystem_upgraded.MovieTicketBookingDemoUpgraded
```
