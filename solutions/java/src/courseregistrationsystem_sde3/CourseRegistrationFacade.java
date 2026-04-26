package courseregistrationsystem_sde3;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class CourseRegistrationFacade {
    private final Map<String, Course> courses;
    private final Map<Integer, Student> students;
    private final List<Registration> registrations;
    private final NotificationService notificationService;

    public CourseRegistrationFacade(NotificationService notificationService) {
        this.courses = new ConcurrentHashMap<>();
        this.students = new ConcurrentHashMap<>();
        this.registrations = new CopyOnWriteArrayList<>();
        this.notificationService = notificationService;
    }

    public void addCourse(Course course) {
        courses.put(course.getCode(), course);
    }

    public void addStudent(Student student) {
        students.put(student.getId(), student);
    }

    public List<Course> searchCourses(String query) {
        String lowerQuery = query.toLowerCase();
        return courses.values().parallelStream()
                .filter(course -> course.getCode().toLowerCase().contains(lowerQuery) 
                               || course.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public boolean registerCourse(Student student, Course course) {
        boolean success = course.tryRegisterStudent();
        
        if (success) {
            Registration registration = new Registration(student, course, Instant.now());
            registrations.add(registration);
            student.addRegisteredCourse(course);
            notificationService.notifyStudentOfRegistration(student, course);
            return true;
        } else {
            notificationService.notifyStudentOfFailure(student, course);
            return false;
        }
    }

    public List<Course> getRegisteredCourses(Student student) {
        return student.getRegisteredCourses();
    }
}
