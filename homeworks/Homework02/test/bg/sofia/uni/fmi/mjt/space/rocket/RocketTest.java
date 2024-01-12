package bg.sofia.uni.fmi.mjt.space.rocket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

public class RocketTest {
    @Test
    void testOfFactoryMethodWithOptionalData() {
        String rocketData = "0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m";

        Rocket rocket = Rocket.of(rocketData);

        assertEquals("0", rocket.id(), "Rocket's id should be correctly parsed!");
        assertEquals("Tsyklon-3", rocket.name(), "Rocket's name should be correctly parsed!");
        assertEquals("https://en.wikipedia.org/wiki/Tsyklon-3", rocket.wiki().get(), "Rocket's wiki should be correctly parsed!");
        assertEquals(39.0, rocket.height().get(), "Rocket's height should be correctly parsed!");
    }

    @Test
    void testOfFactoryMethodWithoutOptionalData() {
        String rocketData = "0,Tsyklon-3,,";

        Rocket rocket = Rocket.of(rocketData);

        assertEquals("0", rocket.id(), "Rocket's id should be correctly parsed!");
        assertEquals("Tsyklon-3", rocket.name(), "Rocket's name should be correctly parsed!");
        assertFalse(rocket.wiki().isPresent(), "Rocket's wiki should be correctly parsed!");
        assertFalse(rocket.height().isPresent(), "Rocket's height should be correctly parsed!");
    }
}
