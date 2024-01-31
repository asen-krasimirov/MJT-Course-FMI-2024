package bg.sofia.uni.fmi.mjt.response;

import bg.sofia.uni.fmi.mjt.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.exceptions.ForbiddenAccessException;
import bg.sofia.uni.fmi.mjt.recipe.Recipe;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class HttpResponseHandler {

    private static final Gson GSON_PARER = new Gson();

    public static void validateResponse(HttpResponse<String> response)
            throws BadRequestException, ForbiddenAccessException {
        if (response.statusCode() == HttpURLConnection.HTTP_FORBIDDEN) {
            throw new ForbiddenAccessException("The access is forbidden.");
        } else if (response.statusCode() != HttpURLConnection.HTTP_OK) {
            throw new BadRequestException("A problem has occurred when sending the request.");
        }
    }

    public static ResponseHolder parseResponse(HttpResponse<String> response)
        throws BadRequestException, ForbiddenAccessException {
        validateResponse(response);

        return GSON_PARER.fromJson(response.body(), ResponseHolder.class);
    }

    private static Recipe parseRecipe(ResponseHolder.Hit.RecipeInfoHolder recipeInfoHolder) {
        return Recipe.of(
            recipeInfoHolder.label(), recipeInfoHolder.dietLabels(), recipeInfoHolder.healthLabels(),
            recipeInfoHolder.ingredientLines(), recipeInfoHolder.totalWeight(), recipeInfoHolder.cuisineType(),
            recipeInfoHolder.mealType(), recipeInfoHolder.dishType()
        );
    }

    public static List<Recipe> extractRecipes(ResponseHolder responseHolder) {
        List<Recipe> recipes = new ArrayList<>();

        for (ResponseHolder.Hit hit : responseHolder.hits()) {
            recipes.add(parseRecipe(hit.recipe()));
        }

        return recipes;
    }

}
