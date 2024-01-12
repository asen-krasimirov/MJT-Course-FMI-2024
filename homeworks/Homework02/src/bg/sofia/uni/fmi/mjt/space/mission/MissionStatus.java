package bg.sofia.uni.fmi.mjt.space.mission;

public enum MissionStatus {
    SUCCESS("Success"),
    FAILURE("Failure"),
    PARTIAL_FAILURE("Partial Failure"),
    PRELAUNCH_FAILURE("Prelaunch Failure");

    private final String value;

    MissionStatus(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public static MissionStatus parseMission(String value) {
        return switch (value) {
            case "Success" -> SUCCESS;
            case "Failure" -> FAILURE;
            case "Partial Failure" -> PARTIAL_FAILURE;
            default -> PRELAUNCH_FAILURE;
        };
    }
}
