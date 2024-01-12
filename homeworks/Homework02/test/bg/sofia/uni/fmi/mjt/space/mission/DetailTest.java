package bg.sofia.uni.fmi.mjt.space.mission;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class DetailTest {
    @Test
    void testOfFactoryMethod() {
        Detail detail = Detail.of("Falcon 9 Block 5 | Starlink V1 L9 & BlackSky");

        assertEquals("Falcon 9 Block 5", detail.rocketName(), "rocketName(...) should be the correct rocket name.");
        assertEquals("Starlink V1 L9 & BlackSky", detail.payload(), "payload(...) should be the correct payload.");
    }
}
