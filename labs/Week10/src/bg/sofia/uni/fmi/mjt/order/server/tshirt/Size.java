package bg.sofia.uni.fmi.mjt.order.server.tshirt;

public enum Size {
    S("S"),
    M("M"),
    L("L"),
    XL("XL"),
    UNKNOWN("UNKNOWN");

    private final String name;

    Size(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Size findByValue(String value) {
        Size result = UNKNOWN;

        for (Size size : values()) {
            if (size.getName().equalsIgnoreCase(value)) {
                result = size;
                break;
            }
        }

        return result;
    }
}
