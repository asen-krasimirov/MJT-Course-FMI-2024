package bg.sofia.uni.fmi.mjt.parser;

import bg.sofia.uni.fmi.mjt.config.APIConfig;
import bg.sofia.uni.fmi.mjt.recipe.field.HealthLabel;
import bg.sofia.uni.fmi.mjt.recipe.field.MealType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UriParserTest {

    UriParser uriParser;

    @BeforeEach
    void setUp() {
        uriParser = UriParser.createUri();
    }

    @Test
    void testParseUriWithoutParameters() {
        String expectedResult = APIConfig.getBaseAccessPoint();

        String result = uriParser.parseUri();

        Assertions.assertEquals(expectedResult, result, "Parsed result should be the correct empty uri.");
    }

    @Test
    void testParsingUriWithOnlyOneKeyword() {
        String expectedResult = APIConfig.getBaseAccessPoint() + "&q=chicken";

        String result = uriParser
            .addKeywords(new String[] {"chicken"})
            .parseUri();

        Assertions.assertEquals(expectedResult, result, "Parsed result should be the correct uri with only 1 keyword.");
    }

    @Test
    void testParsingUriWithManyKeyword() {
        String expectedResult = APIConfig.getBaseAccessPoint() + "&q=chicken%20pork%20lamb";

        String result = uriParser
            .addKeywords(new String[] {"chicken", "pork", "lamb"})
            .parseUri();

        Assertions.assertEquals(expectedResult, result, "Parsed result should be the correct uri with many keywords.");
    }

    @Test
    void testParsingUriWithOnlyOneMealType() {
        String expectedResult = APIConfig.getBaseAccessPoint() + "&mealType=Breakfast";

        String result = uriParser
            .addMealTypes(new MealType[] {MealType.BREAKFAST})
            .parseUri();

        Assertions.assertEquals(expectedResult, result,
            "Parsed result should be the correct uri with only one meal type.");
    }

    @Test
    void testParsingUriWithManyMealType() {
        String expectedResult = APIConfig.getBaseAccessPoint() + "&mealType=Breakfast&mealType=Snack&mealType=Teatime";

        String result = uriParser
            .addMealTypes(new MealType[] {MealType.BREAKFAST, MealType.SNACK, MealType.TEATIME})
            .parseUri();

        Assertions.assertEquals(expectedResult, result,
            "Parsed result should be the correct uri with many meal types.");
    }

    @Test
    void testParsingUriWithOnlyOneHealthLabel() {
        String expectedString = APIConfig.getBaseAccessPoint() + "&health=alcohol-free";

        String result = uriParser
            .addHealthLabel(new HealthLabel[] {HealthLabel.ALCOHOL_FREE})
            .parseUri();

        Assertions.assertEquals(expectedString, result,
            "Parsed result should be the correct uri with only one health label.");
    }

    @Test
    void testParsingUriWithManyHealthLabel() {
        String expectedString = APIConfig.getBaseAccessPoint() + "&health=alcohol-free&health=DASH&health=vegan";

        String result = uriParser
            .addHealthLabel(new HealthLabel[] {HealthLabel.ALCOHOL_FREE, HealthLabel.DASH, HealthLabel.VEGAN})
            .parseUri();

        Assertions.assertEquals(expectedString, result,
            "Parsed result should be the correct uri with many health labels.");
    }

    @Test
    void testParsingUriWithAllParameters() {
        String expectedString = APIConfig.getBaseAccessPoint() +
            "&q=chicken%20pork%20lamb&mealType=Breakfast&mealType=Snack&mealType=Teatime&health=alcohol-free&health=DASH&health=vegan";

        String[] keywords = new String[] {"chicken", "pork", "lamb"};
        MealType[] mealTypes = new MealType[] {MealType.BREAKFAST, MealType.SNACK, MealType.TEATIME};
        HealthLabel[] healthLabels = new HealthLabel[] {HealthLabel.ALCOHOL_FREE, HealthLabel.DASH, HealthLabel.VEGAN};

        String result = uriParser
            .addKeywords(keywords)
            .addMealTypes(mealTypes)
            .addHealthLabel(healthLabels)
            .parseUri();

        Assertions.assertEquals(expectedString, result,
            "Parsed result should be the correct uri with all parameters included.");
    }

    @Test
    void testParsingUriWithRequiredFields() {
        String expectedString = APIConfig.getBaseAccessPoint() +
            "&q=chicken%20pork%20lamb&mealType=Breakfast&mealType=Snack&mealType=Teatime&health=alcohol-free&health=DASH&health=vegan&field=label&field=dietLabels&field=healthLabels&field=ingredientLines&field=totalWeight&field=cuisineType&field=mealType&field=dishType";

        String[] keywords = new String[] {"chicken", "pork", "lamb"};
        MealType[] mealTypes = new MealType[] {MealType.BREAKFAST, MealType.SNACK, MealType.TEATIME};
        HealthLabel[] healthLabels = new HealthLabel[] {HealthLabel.ALCOHOL_FREE, HealthLabel.DASH, HealthLabel.VEGAN};

        String result = uriParser
            .addKeywords(keywords)
            .addMealTypes(mealTypes)
            .addHealthLabel(healthLabels)
            .requiredFieldsOnly()
            .parseUri();

        Assertions.assertEquals(expectedString, result,
            "Parsed result should be the correct uri with all parameters included.");
    }

}
