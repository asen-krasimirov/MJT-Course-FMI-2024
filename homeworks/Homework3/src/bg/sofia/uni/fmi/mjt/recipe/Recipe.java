package bg.sofia.uni.fmi.mjt.recipe;

import bg.sofia.uni.fmi.mjt.recipe.field.CuisineType;
import bg.sofia.uni.fmi.mjt.recipe.field.DietLabel;
import bg.sofia.uni.fmi.mjt.recipe.field.DishType;
import bg.sofia.uni.fmi.mjt.recipe.field.HealthLabel;
import bg.sofia.uni.fmi.mjt.recipe.field.MealType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Recipe(String label, List<DietLabel> dietLabels, List<HealthLabel> healthLabels,
                     List<String> ingredientLines,
                     double totalWeight,
                     List<CuisineType> cuisineType,
                     List<MealType> mealType, List<DishType> dishType) {

    private static String modifyStringForParsing(String str) {
        return str.toUpperCase().replaceAll("[/\\- ]", "_");
    }

    private static List<DietLabel> parseDietLabels(List<String> dietLabelsData) {
        String data;

        List<DietLabel> parsedDietLabel = new ArrayList<>();
        for (String dietLabel : dietLabelsData) {
            data = modifyStringForParsing(dietLabel);
            if (DietLabel.isValidValue(data)) {
                parsedDietLabel.add(DietLabel.valueOf(data));
            } else {
                parsedDietLabel.add(DietLabel.UNKNOWN);
            }
        }

        return parsedDietLabel;
    }

    private static List<HealthLabel> parseHealthLabels(List<String> healthLabelsData) {
        String data;

        List<HealthLabel> parsedHealthLabels = new ArrayList<>();
        for (String healthLabel : healthLabelsData) {
            data = modifyStringForParsing(healthLabel);
            if (HealthLabel.isValidValue(data)) {
                parsedHealthLabels.add(HealthLabel.valueOf(data));
            } else {
                parsedHealthLabels.add(HealthLabel.UNKNOWN);
            }
        }

        return parsedHealthLabels;
    }

    private static List<CuisineType> parseCuisineType(List<String> cuisineTypeData) {
        String data;

        List<CuisineType> parsedCuisineType = new ArrayList<>();
        for (String cuisineType : cuisineTypeData) {
            data = modifyStringForParsing(cuisineType);
            if (CuisineType.isValidValue(data)) {
                parsedCuisineType.add(CuisineType.valueOf(data));
            } else {
                parsedCuisineType.add(CuisineType.UNKNOWN);
            }
        }

        return parsedCuisineType;
    }

    private static List<MealType> parseMealType(List<String> mealTypeData) {
        String data;

        List<MealType> parsedMealType = new ArrayList<>();
        for (String mealType : mealTypeData) {
            data = modifyStringForParsing(mealType);
            if (MealType.isValidValue(data)) {
                parsedMealType.add(MealType.valueOf(data));
            } else {
                parsedMealType.add(MealType.UNKNOWN);
            }
        }

        return parsedMealType;
    }

    private static List<DishType> parseDishType(List<String> dishTypeData) {
        String data;

        List<DishType> parsedDishType = new ArrayList<>();
        for (String dishType : dishTypeData) {
            data = modifyStringForParsing(dishType);
            if (DishType.isValidValue(data)) {
                parsedDishType.add(DishType.valueOf(data));
            } else {
                parsedDishType.add(DishType.UNKNOWN);
            }
        }

        return parsedDishType;
    }

    public static Recipe of(String labelData, List<String> dietLabelsData, List<String> healthLabelsData,
                            List<String> ingredientLinesData,
                            double totalWeightData,
                            List<String> cuisineTypeData,
                            List<String> mealTypeData, List<String> dishTypeData) {

        List<DietLabel> parsedDietLabel = parseDietLabels(dietLabelsData);

        List<HealthLabel> parsedHealthLabels = parseHealthLabels(healthLabelsData);

        List<CuisineType> parsedCuisineType = parseCuisineType(cuisineTypeData);

        List<MealType> parsedMealType = parseMealType(mealTypeData);

        List<DishType> parsedDishType = parseDishType(dishTypeData);

        return new Recipe(labelData, parsedDietLabel, parsedHealthLabels, ingredientLinesData, totalWeightData,
            parsedCuisineType, parsedMealType, parsedDishType);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Recipe recipe)) {
            return false;
        }

        return Double.compare(recipe.totalWeight, totalWeight) == 0 &&
            label.equals(recipe.label) &&
            dietLabels.equals(recipe.dietLabels) &&
            healthLabels.equals(recipe.healthLabels) &&
            ingredientLines.equals(recipe.ingredientLines) &&
            cuisineType.equals(recipe.cuisineType) &&
            mealType.equals(recipe.mealType) &&
            dishType.equals(recipe.dishType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(label);

        result += Objects.hash(totalWeight);

        result += Objects.hash(dietLabels);
        result += Objects.hash(healthLabels);
        result += Objects.hash(ingredientLines);
        result += Objects.hash(cuisineType);
        result += Objects.hash(mealType);
        result += Objects.hash(dishType);

        return result;
    }

}
