package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class RgbBulbTest {

    static RgbBulb device;

    @BeforeAll
    static void setUpTestCase() {
        IoTDeviceBase.uniqueNumberDevice = 0;

        LocalDateTime localDateTime = LocalDateTime.of(2023, 11, 18, 17, 36);
        device = new RgbBulb("Device1", 10, localDateTime);
    }

    @Test
    void testGetCorrectId() {
        Assertions.assertEquals("BLB-Device1-0", device.getId(),
            "getId() should return correct id in format <short name of device type>-<device name>-<unique number per device type>.");
    }

    @Test
    void testGetCorrectDeviceName() {
        Assertions.assertEquals("Device1", device.getName(), "getName() should return correct device name.");
    }

    @Test
    void testGetCorrectPowerConsumption() {
        Assertions.assertEquals(10, device.getPowerConsumption(),
            "getPowerConsumption() should return correct power consumption.");
    }

    @Test
    void testGetCorrectInstallationDateTime() {
        LocalDateTime correctDateTime = LocalDateTime.of(2023, 11, 18, 17, 36);

        Assertions.assertEquals(correctDateTime, device.getInstallationDateTime(),
            "getInstallationDateTime() should return correct installation time.");
    }

    @Test
    void testGetCorrectDeviceType() {
        DeviceType correctDeviceType = DeviceType.BULB;

        Assertions.assertEquals(correctDeviceType, device.getType(), "getType() should return correct device type.");
    }

}
