package courseregistrationsystem_upgraded;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Course {
    private final String code;
    private final String name;
    private final String instructor;
    private final int maxCapacity;
    private final AtomicInteger enrolledStudents;
    private final ReentrantLock courseLock;

    public Course(String code, String name, String instructor, int maxCapacity) {
        this.code = code;
        this.name = name;
        this.instructor = instructor;
        this.maxCapacity = maxCapacity;
        this.enrolledStudents = new AtomicInteger(0);
        this.courseLock = new ReentrantLock();
    }

    public boolean tryRegisterStudent() {
        courseLock.lock();
        try {
            if (enrolledStudents.get() < maxCapacity) {
                enrolledStudents.incrementAndGet();
                return true;
            }
            return false;
        } finally {
            courseLock.unlock();
        }
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getInstructor() {
        return instructor;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getEnrolledStudents() {
        return enrolledStudents.get();
    }
}
