package bg.sofia.uni.fmi.mjt.config;

public class APIConfig {

    public static final String ACCESS_POINT = "https://api.edamam.com/api/recipes/v2?type=public";

    public static final String APP_ID = "5eca9706";

    public static final String APP_KEY = "c1061e0819eab1493a26fac6bfc6cb06";

    public static String parsedAccessPoint = null;

    public static final int MAX_RESULTS_PRE_PAGE = 20;

    public static final String[] REQUIRED_FIELDS = new String[] {
        "label", "dietLabels", "healthLabels", "ingredientLines", "totalWeight", "cuisineType", "mealType", "dishType",
    };

    public static String getBaseAccessPoint() {
        if (parsedAccessPoint != null) {
            return parsedAccessPoint;
        }

        parsedAccessPoint = ACCESS_POINT + "&app_id=" + APP_ID + "&app_key=" + APP_KEY;

        return parsedAccessPoint;
    }

}
