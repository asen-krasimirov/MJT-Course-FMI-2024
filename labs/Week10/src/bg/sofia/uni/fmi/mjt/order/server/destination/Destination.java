package bg.sofia.uni.fmi.mjt.order.server.destination;

public enum Destination {
    EUROPE("EUROPE"),
    NORTH_AMERICA("NORTH_AMERICA"),
    AUSTRALIA("AUSTRALIA"),
    UNKNOWN("UNKNOWN");

    private final String name;

    Destination(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Destination findByValue(String value) {
        Destination result = UNKNOWN;

        for (Destination destination : values()) {
            if (destination.getName().equalsIgnoreCase(value)) {
                result = destination;
                break;
            }
        }

        return result;
    }
}
