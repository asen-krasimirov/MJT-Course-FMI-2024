package bg.sofia.uni.fmi.mjt.space.rocket;

public enum RocketStatus {
    STATUS_RETIRED("StatusRetired"),
    STATUS_ACTIVE("StatusActive");

    private final String value;

    RocketStatus(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public static RocketStatus parseRocket(String value) {
        return switch (value) {
            case "StatusRetired" -> STATUS_RETIRED;
            default -> STATUS_ACTIVE;
        };
    }
}
