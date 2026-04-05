package courseregistrationsystem_sde3;

import java.util.concurrent.atomic.AtomicInteger;

public class Course {
    private final String courseId;
    private final AtomicInteger availableSeats;

    public Course(String courseId, int totalCapacity) {
        this.courseId = courseId;
        this.availableSeats = new AtomicInteger(totalCapacity);
    }

    public String getCourseId() { return courseId; }
    public int getAvailableSeats() { return availableSeats.get(); }

    public boolean enrollSeat() {
        while (true) {
            int current = availableSeats.get();
            if (current <= 0) {
                return false; // Full
            }
            if (availableSeats.compareAndSet(current, current - 1)) {
                return true;
            }
        }
    }
    
    public void dropSeat() {
        availableSeats.incrementAndGet();
    }
}
