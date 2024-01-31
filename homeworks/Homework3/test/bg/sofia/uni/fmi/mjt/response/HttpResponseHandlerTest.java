package bg.sofia.uni.fmi.mjt.response;

import bg.sofia.uni.fmi.mjt.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.exceptions.ForbiddenAccessException;
import bg.sofia.uni.fmi.mjt.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.recipe.field.CuisineType;
import bg.sofia.uni.fmi.mjt.recipe.field.DietLabel;
import bg.sofia.uni.fmi.mjt.recipe.field.DishType;
import bg.sofia.uni.fmi.mjt.recipe.field.HealthLabel;
import bg.sofia.uni.fmi.mjt.recipe.field.MealType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpResponseHandlerTest {

    private static String getResponseBody(String fileName) {
        StringBuilder responseBody = new StringBuilder();

        File file = new File(fileName);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String st;
            while ((st = br.readLine()) != null) {
                responseBody.append(st);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("A problem occurred while reading from responseBody file.", e);
        }
        return responseBody.toString();
    }

    @Test
    void testHandleResponseWithValidResponseData() throws BadRequestException, ForbiddenAccessException {
        HttpResponse<String> response = mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(200);

        String responseBody = getResponseBody("testResponseBody.txt");
        when(response.body()).thenReturn(responseBody);

        List<Recipe> expectedResult = List.of(new Recipe(
                "Moroccan Spice Mix", List.of(DietLabel.BALANCED, DietLabel.LOW_SODIUM),
                List.of(HealthLabel.VEGAN, HealthLabel.DASH, HealthLabel.ALCOHOL_FREE),
                List.of("1/4 tsp red pepper flakes", "1/2 tsp ground turmeric", "1 tsp sweet paprika"), 12.91,
                List.of(CuisineType.MIDDLE_EASTERN),
                List.of(MealType.LUNCH_DINNER, MealType.SNACK),
                List.of(DishType.MAIN_COURSE)
            )
        );

        ResponseHolder responseHolder = HttpResponseHandler.parseResponse(response);

        List<Recipe> result = HttpResponseHandler.extractRecipes(responseHolder);

        Assertions.assertIterableEquals(expectedResult, result,
            "handleResponse(...) should parse the correct information for one recipe received.");
    }

    @Test
    void testValidateResponseWithForbiddenAccessStatusCode() {
        HttpResponse<String> response = mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(403);

        Assertions.assertThrows(ForbiddenAccessException.class, () -> HttpResponseHandler.validateResponse(response),
            "handleResponse(...) should throw ForbiddenAccessException when response is with 403 statusCode.");
    }

    @Test
    void testValidateResponseWithBadRequestStatusCode() {
        HttpResponse<String> response = mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(400);

        Assertions.assertThrows(BadRequestException.class, () -> HttpResponseHandler.validateResponse(response),
            "handleResponse(...) should throw BadRequestException when response is with 400 statusCode.");
    }

}
