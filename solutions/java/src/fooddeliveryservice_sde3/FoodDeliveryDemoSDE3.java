package fooddeliveryservice_sde3;

public class FoodDeliveryDemoSDE3 {
    public static void main(String[] args) {
        new DeliveryDispatcher(); // Boot up detached async processors

        OrderManager manager = new OrderManager();
        Order order = new Order("ORD_777");
        manager.createOrder(order);

        System.out.println("Restaurant accepts the order...");
        manager.updateOrderStatus("ORD_777", OrderStatus.PLACED, OrderStatus.PREPARING);
    }
}
