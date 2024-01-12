package bg.sofia.uni.fmi.mjt.space.mission;

public record Detail(String rocketName, String payload) {
    public static Detail of(String detailInfo) {
        String[] detailTokens = detailInfo.split("\\Q | \\E");
        return new Detail(detailTokens[0], detailTokens[1]);
    }
}
