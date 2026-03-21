package courseregistrationsystem_upgraded;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CourseRegistrationDemoUpgraded {
    public static void run() {
        NotificationService notificationService = new NotificationService();
        CourseRegistrationFacade registrationSystem = new CourseRegistrationFacade(notificationService);

        // Create a course with very limited capacity to test concurrency
        Course course1 = new Course("CS101", "Introduction to Programming", "John Doe", 5);
        Course course2 = new Course("CS201", "Data Structures", "Jane Smith", 30);
        registrationSystem.addCourse(course1);
        registrationSystem.addCourse(course2);

        // Search for courses (tests parallel stream internally)
        List<Course> searchResults = registrationSystem.searchCourses("CS");
        System.out.println("Search Results:");
        for (Course course : searchResults) {
            System.out.println(course.getCode() + " - " + course.getName());
        }

        // Test concurrent registration: 20 students competing for 5 spots in CS101
        int numStudents = 20;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numStudents);
        
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < numStudents; i++) {
            Student s = new Student(i + 1, "Student " + (i + 1), "student" + (i + 1) + "@example.com");
            students.add(s);
            registrationSystem.addStudent(s);
        }

        System.out.println("\n--- Starting concurrent registration flash rush ---");
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (Student student : students) {
            executor.submit(() -> {
                boolean success = registrationSystem.registerCourse(student, course1);
                if (success) {
                    successCount.incrementAndGet();
                } else {
                    failureCount.incrementAndGet();
                }
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        executor.shutdown();
        
        // Wait a bit for async notifications to flush to standard out
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
        
        notificationService.shutdown();

        System.out.println("\n--- Results ---");
        System.out.println("Course Capacity: " + course1.getMaxCapacity());
        System.out.println("Successful Registrations: " + successCount.get());
        System.out.println("Failed Registrations (Waitlisted): " + failureCount.get());
        System.out.println("Actual Enrolled in Course object: " + course1.getEnrolledStudents());
        
        if (successCount.get() == course1.getMaxCapacity() && course1.getEnrolledStudents() == course1.getMaxCapacity()) {
            System.out.println("SUCCESS: Strict capacity limit maintained under concurrent load without global locks.");
        } else {
            System.out.println("FAILED: Capacity limit breached or mismatched logic.");
        }
    }

    public static void main(String[] args) {
        run();
    }
}
