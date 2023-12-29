package bg.sofia.uni.fmi.mjt.order.server.tshirt;

public enum Color {
    BLACK("BLACK"),
    WHITE("WHITE"),
    RED("RED"),
    UNKNOWN("UNKNOWN");

    private final String name;

    Color(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Color findByValue(String value) {
        Color result = UNKNOWN;

        for (Color color : values()) {
            if (color.getName().equalsIgnoreCase(value)) {
                result = color;
                break;
            }
        }

        return result;
    }
}
