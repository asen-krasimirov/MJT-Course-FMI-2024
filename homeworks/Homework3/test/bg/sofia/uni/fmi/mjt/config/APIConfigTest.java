package bg.sofia.uni.fmi.mjt.config;

import bg.sofia.uni.fmi.mjt.TestConfigs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class APIConfigTest {

    private static String expectedResult;

    @BeforeAll
    static void setUpTestCase() {
        expectedResult = TestConfigs.getExpectedAccessPoint();
    }

    @Test
    void testGetBaseAccessPointSingleTime() {
        String result = APIConfig.getBaseAccessPoint();

        Assertions.assertEquals(expectedResult, result,
            "getBaseAccessPoint() should return the correct base point when called once.");
    }

    @Test
    void testGetBaseAccessPointMultipleTimes() {
        APIConfig.getBaseAccessPoint();
        APIConfig.getBaseAccessPoint();

        String result = APIConfig.getBaseAccessPoint();

        Assertions.assertEquals(expectedResult, result,
            "getBaseAccessPoint() should return the correct base point when called multiple times.");
    }

}
