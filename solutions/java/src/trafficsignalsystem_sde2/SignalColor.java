package trafficsignalsystem_sde2;

public enum SignalColor {
    RED(60), GREEN(45), YELLOW(5);

    private final int defaultDurationSeconds;

    SignalColor(int defaultDurationSeconds) {
        this.defaultDurationSeconds = defaultDurationSeconds;
    }

    public int getDefaultDurationSeconds() {
        return defaultDurationSeconds;
    }
}
