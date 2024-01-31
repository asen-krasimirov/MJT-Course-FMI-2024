package bg.sofia.uni.fmi.mjt.recipe;

import bg.sofia.uni.fmi.mjt.recipe.field.CuisineType;
import bg.sofia.uni.fmi.mjt.recipe.field.DietLabel;
import bg.sofia.uni.fmi.mjt.recipe.field.DishType;
import bg.sofia.uni.fmi.mjt.recipe.field.HealthLabel;
import bg.sofia.uni.fmi.mjt.recipe.field.MealType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RecipeTest {

    @Test
    void testRecipeOfWithValidData() {
        Recipe expectedResult = new Recipe(
            "Moroccan Spice Mix", List.of(DietLabel.BALANCED, DietLabel.LOW_SODIUM),
            List.of(HealthLabel.VEGAN, HealthLabel.DASH, HealthLabel.ALCOHOL_FREE),
            List.of("1/4 tsp red pepper flakes", "1/2 tsp ground turmeric", "1 tsp sweet paprika"), 12.91,
            List.of(CuisineType.MIDDLE_EASTERN),
            List.of(MealType.LUNCH_DINNER, MealType.SNACK),
            List.of(DishType.MAIN_COURSE)
        );

        Recipe result = Recipe.of("Moroccan Spice Mix", List.of("Balanced", "Low-Sodium"),
            List.of("Vegan", "DASH", "Alcohol-Free"),
            List.of("1/4 tsp red pepper flakes", "1/2 tsp ground turmeric", "1 tsp sweet paprika"), 12.91,
            List.of("middle eastern"), List.of("lunch/dinner", "snack"), List.of("main course"));

        Assertions.assertEquals(expectedResult, result, "of(...) should correctly parse the data.");
    }

    @Test
    void testRecipeOfWithInvalidData() {
        Recipe expectedResult =
            new Recipe("", List.of(DietLabel.UNKNOWN),
                List.of(HealthLabel.UNKNOWN),
                List.of(), 0.0,
                List.of(CuisineType.UNKNOWN),
                List.of(MealType.UNKNOWN),
                List.of(DishType.UNKNOWN));

        Recipe result = Recipe.of("", List.of("Test"),
            List.of("Test"), List.of(), 0.0, List.of("test"), List.of("test"), List.of("test"));

        Assertions.assertEquals(expectedResult, result, "of(...) should correctly parse the data.");
    }

}
