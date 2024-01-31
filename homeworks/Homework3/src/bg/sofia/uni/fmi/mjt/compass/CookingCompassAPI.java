package bg.sofia.uni.fmi.mjt.compass;

import bg.sofia.uni.fmi.mjt.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.exceptions.ConnectionFailedException;
import bg.sofia.uni.fmi.mjt.exceptions.ForbiddenAccessException;
import bg.sofia.uni.fmi.mjt.recipe.field.HealthLabel;
import bg.sofia.uni.fmi.mjt.recipe.field.MealType;
import bg.sofia.uni.fmi.mjt.recipe.Recipe;

import java.util.List;

public interface CookingCompassAPI {

    List<Recipe> findRecipe(String[] keywords, MealType[] mealTypes, HealthLabel[] healthLabels)
        throws ConnectionFailedException, BadRequestException, ForbiddenAccessException;

}
