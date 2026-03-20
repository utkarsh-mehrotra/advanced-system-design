package fooddeliveryservice_upgraded;

public class MenuItem {
    private final String id;
    private final String name;
    private final String description;
    private final double price;

    public MenuItem(String id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}
