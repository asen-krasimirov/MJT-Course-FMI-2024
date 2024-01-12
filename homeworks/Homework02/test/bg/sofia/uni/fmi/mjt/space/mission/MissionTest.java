package bg.sofia.uni.fmi.mjt.space.mission;

import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class MissionTest {
    @Test
    void testOfFactoryMethodWithOptionalData() {
        String missionData = "0,SpaceX,\"LC-39A, Kennedy Space Center, Florida, USA\",\"Fri Aug 07, 2020\",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,\"50.0 \",Success";

        Mission mission = Mission.of(missionData);

        Assertions.assertEquals("0", mission.id(), "Mission's id should be correctly parsed.");
        Assertions.assertEquals("SpaceX", mission.company(), "Mission's company should be correctly parsed.");
        Assertions.assertEquals("LC-39A, Kennedy Space Center, Florida, USA", mission.location(), "Mission's location should be correctly parsed.");
        Assertions.assertEquals(LocalDate.of(2020, 8, 7), mission.date(), "Mission's date should be correctly parsed.");
        Assertions.assertEquals("Falcon 9 Block 5", mission.detail().rocketName(), "Mission's rocketName in detail should be correctly parsed.");
        Assertions.assertEquals("Starlink V1 L9 & BlackSky", mission.detail().payload(), "Mission's payload in detail should be correctly parsed.");
        Assertions.assertEquals(RocketStatus.STATUS_ACTIVE, mission.rocketStatus(), "Mission's rocketStatus should be correctly parsed.");
        Assertions.assertTrue(mission.cost().isPresent(), "Mission's cost should have been casted to a value.");
        Assertions.assertEquals(50.0, mission.cost().get(), "Mission's cost should be correctly parsed.");
        Assertions.assertEquals(MissionStatus.SUCCESS, mission.missionStatus(), "Mission's missionStatus should be correctly parsed.");
    }

    @Test
    void testOfFactoryMethodWithoutOptionalData() {
        String missionData = "0,SpaceX,\"LC-39A, Kennedy Space Center, Florida, USA\",\"Fri Aug 07, 2020\",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,,Success";

        Mission mission = Mission.of(missionData);

        Assertions.assertEquals("0", mission.id(), "Mission's id should be correctly parsed.");
        Assertions.assertEquals("SpaceX", mission.company(), "Mission's company should be correctly parsed.");
        Assertions.assertEquals("LC-39A, Kennedy Space Center, Florida, USA", mission.location(), "Mission's location should be correctly parsed.");
        Assertions.assertEquals(LocalDate.of(2020, 8, 7), mission.date(), "Mission's date should be correctly parsed.");
        Assertions.assertEquals("Falcon 9 Block 5", mission.detail().rocketName(), "Mission's rocketName in detail should be correctly parsed.");
        Assertions.assertEquals("Starlink V1 L9 & BlackSky", mission.detail().payload(), "Mission's payload in detail should be correctly parsed.");
        Assertions.assertEquals(RocketStatus.STATUS_ACTIVE, mission.rocketStatus(), "Mission's rocketStatus should be correctly parsed.");
        Assertions.assertFalse(mission.cost().isPresent(), "Mission's cost should be correctly parsed.");
        Assertions.assertEquals(MissionStatus.SUCCESS, mission.missionStatus(), "Mission's missionStatus should be correctly parsed.");
    }
}
