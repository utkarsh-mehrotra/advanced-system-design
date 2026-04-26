package stackoverflow_sde3;

public class Tag {
    private final String name;

    public Tag(String name) {
        this.name = name.toLowerCase();
    }

    public String getName() { return name; }
}
