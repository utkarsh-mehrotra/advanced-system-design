package courseregistrationsystem_upgraded;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Student {
    private final int id;
    private final String name;
    private final String email;
    private final List<Course> registeredCourses;

    public Student(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.registeredCourses = new CopyOnWriteArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Course> getRegisteredCourses() {
        return registeredCourses;
    }
    
    public void addRegisteredCourse(Course course) {
        this.registeredCourses.add(course);
    }
}
