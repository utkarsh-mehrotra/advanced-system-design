package courseregistrationsystem_sde3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RegistrationManager {
    private final Map<String, Course> catalog = new ConcurrentHashMap<>();
    // ReadHeavy operations like browsing need Reader locks
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void addCourse(Course course) {
        catalog.put(course.getCourseId(), course);
    }

    public void browseCourses() {
        rwLock.readLock().lock();
        try {
            catalog.values().forEach(c -> 
                System.out.println(c.getCourseId() + " -> Seats: " + c.getAvailableSeats())
            );
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void register(String courseId, String studentId) {
        Course c = catalog.get(courseId);
        if (c != null) {
            if (c.enrollSeat()) {
                System.out.println("RegistrationManager: " + studentId + " enrolled in " + courseId);
            } else {
                System.out.println("RegistrationManager: " + courseId + " FULL. Adding " + studentId + " to waitlist via Event Bus.");
                EventBus.getInstance().publish("COURSE_FULL_WAITLIST", courseId + ":" + studentId);
            }
        }
    }
}
