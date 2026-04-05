package linkedin_sde3;

public class LinkedInDemoSDE3 {
    public static void main(String[] args) {
        // Init async workers
        new NewsFeedService();

        LinkedInManager manager = new LinkedInManager();
        System.out.println("User adding post...");
        manager.addPost("U_100", "Hello SDE3 Architecture!");
    }
}
