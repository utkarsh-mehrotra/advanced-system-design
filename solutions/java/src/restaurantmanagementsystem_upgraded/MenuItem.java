package restaurantmanagementsystem_upgraded;

import java.math.BigDecimal;

public class MenuItem {
    private final int id;
    private final String name;
    private final String description;
    private final BigDecimal price; // SDE3 Upgrade

    public MenuItem(int id, String name, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
}
