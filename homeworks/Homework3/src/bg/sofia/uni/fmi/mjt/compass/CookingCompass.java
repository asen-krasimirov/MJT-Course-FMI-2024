package bg.sofia.uni.fmi.mjt.compass;

import bg.sofia.uni.fmi.mjt.config.APIConfig;
import bg.sofia.uni.fmi.mjt.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.exceptions.ConnectionFailedException;
import bg.sofia.uni.fmi.mjt.exceptions.ForbiddenAccessException;
import bg.sofia.uni.fmi.mjt.parser.UriParser;
import bg.sofia.uni.fmi.mjt.recipe.field.HealthLabel;
import bg.sofia.uni.fmi.mjt.recipe.field.MealType;
import bg.sofia.uni.fmi.mjt.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.response.HttpResponseHandler;
import bg.sofia.uni.fmi.mjt.response.ResponseHolder;
import bg.sofia.uni.fmi.mjt.sender.SynchronousHttpRequestSender;

import java.net.http.HttpResponse;
import java.util.List;

public class CookingCompass implements CookingCompassAPI {

    private final SynchronousHttpRequestSender httpRequestSender;

    public CookingCompass() {
        httpRequestSender = new SynchronousHttpRequestSender();
    }

    private ResponseHolder sendRequest(String uriString)
        throws ConnectionFailedException, BadRequestException, ForbiddenAccessException {
        HttpResponse<String> response = httpRequestSender.sendRequest(uriString);

        return HttpResponseHandler.parseResponse(response);
    }

    @Override
    public List<Recipe> findRecipe(String[] keywords, MealType[] mealTypes, HealthLabel[] healthLabels)
        throws ConnectionFailedException, BadRequestException, ForbiddenAccessException {
        String uriString = UriParser.createUri()
            .addKeywords(keywords)
            .addMealTypes(mealTypes)
            .addHealthLabel(healthLabels)
            .requiredFieldsOnly()
            .parseUri();

        ResponseHolder responseHolder = sendRequest(uriString);

        List<Recipe> recipes = HttpResponseHandler.extractRecipes(responseHolder);

        if (responseHolder.count() > APIConfig.MAX_RESULTS_PRE_PAGE) {
            responseHolder = sendRequest(responseHolder._links().next().href());

            recipes.addAll(HttpResponseHandler.extractRecipes(responseHolder));
        }

        return recipes;
    }

}
