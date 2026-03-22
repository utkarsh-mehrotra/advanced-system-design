package vendingmachine_sde2;

public enum Note {
    ONE(1.0),
    FIVE(5.0),
    TEN(10.0),
    TWENTY(20.0);

    private final double value;

    Note(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
