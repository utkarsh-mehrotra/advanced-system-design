package courseregistrationsystem_sde3;

public class CourseDemoSDE3 {
    public static void main(String[] args) {
        new WaitlistService(); // Init Background Workers

        RegistrationManager manager = new RegistrationManager();
        manager.addCourse(new Course("CS101", 1));

        manager.browseCourses();

        manager.register("CS101", "Student_A");
        manager.register("CS101", "Student_B"); // Should hit waitlist
    }
}
