package bg.sofia.uni.fmi.mjt.parser;

import bg.sofia.uni.fmi.mjt.config.APIConfig;
import bg.sofia.uni.fmi.mjt.recipe.field.HealthLabel;
import bg.sofia.uni.fmi.mjt.recipe.field.MealType;

public class UriParser {

    StringBuilder resultUri;

    public static UriParser createUri() {
        return new UriParser();
    }

    private UriParser() {
        resultUri = new StringBuilder(APIConfig.getBaseAccessPoint());
    }

    public UriParser addKeywords(String[] keywords) {
        if (keywords.length != 0) {
            resultUri.append("&q=");

            for (int i = 0; i < keywords.length; ++i) {
                resultUri.append(keywords[i]);

                if (i + 1 < keywords.length) {
                    resultUri.append("%20");
                }
            }
        }

        return this;
    }

    public UriParser addMealTypes(MealType[] mealTypes) {
        for (MealType mealType : mealTypes) {
            if (mealType != MealType.UNKNOWN) {
                resultUri.append("&mealType=").append(mealType.getValue());
            }
        }

        return this;
    }

    public UriParser addHealthLabel(HealthLabel[] healthLabels) {
        for (HealthLabel healthLabel : healthLabels) {
            if (healthLabel != HealthLabel.UNKNOWN) {
                resultUri.append("&health=").append(healthLabel.getValue());
            }
        }

        return this;
    }

    public UriParser requiredFieldsOnly() {
        for (String requiredField : APIConfig.REQUIRED_FIELDS) {
            resultUri.append("&field=").append(requiredField);
        }

        return this;
    }

    public String parseUri() {
        return resultUri.toString();
    }

}
