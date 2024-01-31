package bg.sofia.uni.fmi.mjt.recipe.field;

public enum MealType {

    BREAKFAST("Breakfast"),

    BRUNCH("Brunch"),

    LUNCH_DINNER("Lunch"),

    SNACK("Snack"),

    TEATIME("Teatime"),

    UNKNOWN("Unknown");

    private final String value;

    MealType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValidValue(String string) {
        for (MealType mealType : MealType.values()) {
            if (mealType.name().equals(string)) {
                return true;
            }
        }

        return false;
    }

}
