package coffeevendingmachine_sde3;

public class CoffeeDemoSDE3 {
    public static void main(String[] args) {
        new HardwareDispenser(); // Boot detached motor listeners

        CoffeeController controller = new CoffeeController();
        
        System.out.println("User selects Espresso...");
        controller.requestBrew("Espresso");
        
        System.out.println("User selects Latte concurrently...");
        controller.requestBrew("Latte"); // Should be handled lock-free cleanly
    }
}
