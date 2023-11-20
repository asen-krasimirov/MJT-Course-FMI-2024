package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;

public class IoTDeviceBaseTest {

    static IoTDeviceBase device;

    @BeforeAll
    static void setUpTestCase() {
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(4);
        device = new AmazonAlexa("Device1", 100, localDateTime); // using stub
    }

    @AfterAll
    static void tearDownTestCase() {
        IoTDeviceBase.uniqueNumberDevice = 0;
    }

    @Test
    void getCorrectRegistration() {
        device.setRegistration(LocalDateTime.now().minusHours(4));

        Assertions.assertEquals(4, device.getRegistration(),
            "getRegistration() should return correct duration between registration LocalDateTime and LocalDateTime.now().");
    }

    @Test
    void getCorrectPowerConsumptionKWh() {
        Assertions.assertEquals(400.0, device.getPowerConsumptionKWh(),
            "getPowerConsumptionKWh() should return the right power consumption in KWh.");
    }
}
