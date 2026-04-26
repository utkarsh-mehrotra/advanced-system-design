package carrentalsystem_sde3;

import java.util.UUID;

public class Reservation {
    private final String reservationId;
    private final Customer customer;
    private final Car car;
    private final int days;
    private final double totalCost;

    public Reservation(Customer customer, Car car, int days, double totalCost) {
        this.reservationId = UUID.randomUUID().toString();
        this.customer = customer;
        this.car = car;
        this.days = days;
        this.totalCost = totalCost;
    }

    public String getReservationId() { return reservationId; }
    public Car getCar() { return car; }
    public double getTotalCost() { return totalCost; }
}
