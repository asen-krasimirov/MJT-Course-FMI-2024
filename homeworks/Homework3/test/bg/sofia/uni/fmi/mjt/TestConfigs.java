package bg.sofia.uni.fmi.mjt;

public class TestConfigs {

    private static final String EXPECTED_ACCESS_POINT = "https://api.edamam.com/api/recipes/v2?type=public";

    private static final String EXPECTED_APP_ID = "5eca9706";

    private static final String EXPECTED_APP_KEY = "c1061e0819eab1493a26fac6bfc6cb06";

    private static String expectedAccessPoint = null;

    public static String getExpectedAccessPoint() {
        if (expectedAccessPoint != null) {
            return expectedAccessPoint;
        }

        expectedAccessPoint = EXPECTED_ACCESS_POINT + "&app_id=" + EXPECTED_APP_ID + "&app_key=" + EXPECTED_APP_KEY;

        return expectedAccessPoint;
    }

}
