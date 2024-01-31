package bg.sofia.uni.fmi.mjt.recipe.field;

public enum CuisineType {

    AMERICAN("american"),

    ASIAN("asian"),

    BRITISH("british"),

    CARIBBEAN("caribbean"),

    CENTRAL_EUROPE("central europe"),

    CHINESE("chinese"),

    EASTERN_EUROPE("eastern europe"),

    FRENCH("french"),

    GREEK("greek"),

    INDIAN("indian"),

    ITALIAN("italian"),

    JAPANESE("japanese"),

    KOREAN("korean"),

    KOSHER("kosher"),

    MEDITERRANEAN("mediterranean"),

    MEXICAN("mexican"),

    MIDDLE_EASTERN("middle eastern"),

    NORDIC("nordic"),

    SOUTH_AMERICAN("south american"),

    SOUTH_EAST_ASIAN("south east asian"),

    WORLD("world"),

    UNKNOWN("unknown");

    private final String value;

    CuisineType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValidValue(String string) {
        for (CuisineType cuisineType : CuisineType.values()) {
            if (cuisineType.name().equals(string)) {
                return true;
            }
        }

        return false;
    }

}
