package coffeevendingmachine_sde2;

public class CoffeeDemoSDE2 {
    public static void main(String[] args) {
        CoffeeMachine machine = new CoffeeMachine();
        
        System.out.println("Starting thread-safe brew requests...");
        machine.brewCoffee("Espresso");
        machine.brewCoffee("Latte");
    }
}
