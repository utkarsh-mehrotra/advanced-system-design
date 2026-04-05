package elevatorsystem_sde3;

public class ElevatorDemoSDE3 {
    public static void main(String[] args) {
        ElevatorController controller = new ElevatorController();
        Elevator e1 = new Elevator("E1");
        controller.registerElevator(e1);

        controller.requestElevator(0, 5);

        System.out.println("Simulating sensor trip on Floor 5...");
        e1.sensorReachedFloor(5);
    }
}
