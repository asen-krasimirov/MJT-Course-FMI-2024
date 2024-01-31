package bg.sofia.uni.fmi.mjt.recipe.field;

import bg.sofia.uni.fmi.mjt.recipe.Recipe;

import java.util.Arrays;
import java.util.Objects;

public enum DietLabel {

    BALANCED("balanced"),

    HIGH_FIBER("high-fiber"),

    HIGH_PROTEIN("high-protein"),

    LOW_CARB("low-carb"),

    LOW_FAT("low-fat"),

    LOW_SODIUM("low-sodium"),

    UNKNOWN("unknown");

    private final String value;

    DietLabel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValidValue(String string) {
        for (DietLabel dietLabel : DietLabel.values()) {
            if (dietLabel.name().equals(string)) {
                return true;
            }
        }

        return false;
    }

}
