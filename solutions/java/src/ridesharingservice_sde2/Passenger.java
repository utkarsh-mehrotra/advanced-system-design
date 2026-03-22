package ridesharingservice_sde2;

public class Passenger {
    private final String id;
    private final String name;
    private final String contact;

    public Passenger(String id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }

    public String getId() { return id; }
    public String getName() { return name; }
}
