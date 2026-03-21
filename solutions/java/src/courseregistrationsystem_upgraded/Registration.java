package courseregistrationsystem_upgraded;

import java.time.Instant;

public class Registration {
    private final Student student;
    private final Course course;
    private final Instant registrationTime;

    public Registration(Student student, Course course, Instant registrationTime) {
        this.student = student;
        this.course = course;
        this.registrationTime = registrationTime;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public Instant getRegistrationTime() {
        return registrationTime;
    }
}
