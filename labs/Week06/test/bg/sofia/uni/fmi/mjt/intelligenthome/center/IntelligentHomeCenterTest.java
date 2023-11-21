package bg.sofia.uni.fmi.mjt.intelligenthome.center;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;

import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import bg.sofia.uni.fmi.mjt.intelligenthome.storage.DeviceStorage;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;

import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IntelligentHomeCenterTest {

    @Mock
    static DeviceStorage storage;

    static IntelligentHomeCenter homeCenter;

    @BeforeAll
    static void setUpTestCase() {
        storage = mock();

        homeCenter = new IntelligentHomeCenter(storage);
    }

    @Test
    void testRegisterNullDevice() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.register(null),
            "register(...) should throw IllegalArgumentException when is called with null obj.");
    }

    @Test
    void testRegisterDeviceAlreadyRegistered() {
        IoTDevice device = mock();
        when(device.getId()).thenReturn("SPKR-Device1-0");

        when(storage.exists(device.getId())).thenReturn(true);

        Assertions.assertThrows(DeviceAlreadyRegisteredException.class, () -> homeCenter.register(device),
            "register(...) should throw DeviceAlreadyRegisteredException when is called with already registered device.");

    }

    @Test
    void testRegisterCorrectDevice() {
        IoTDevice device = mock();

        when(storage.exists(device.getId())).thenReturn(false);

        Assertions.assertDoesNotThrow(() -> homeCenter.register(device),
            "register(...) should not throw when correct data is passed.");
    }

    @Test
    void testUnregisterNullDevice() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.unregister(null),
            "unregister(...) should throw IllegalArgumentException when is called with null obj.");
    }

    @Test
    void testUnregisterDeviceNotFound() {
        IoTDevice device = mock();
        when(device.getId()).thenReturn("SPKR-Device1-0");

        when(storage.exists(device.getId())).thenReturn(false);

        Assertions.assertThrows(DeviceNotFoundException.class, () -> homeCenter.unregister(device),
            "unregister(...) should throw DeviceNotFoundException when is called with device not registered yet.");
    }

    @Test
    void testUnregisterCorrectDevice() {
        IoTDevice device = mock();

        when(storage.exists(device.getId())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> homeCenter.unregister(device),
            "unregister(...) should not throw when correct data is passed.");
    }

    @Test
    void testGetDeviceByNullId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.getDeviceById(null),
            "getDeviceById(...) should throw IllegalArgumentException when is called with null obj.");
    }

    @Test
    void testGetDeviceByEmptyId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.getDeviceById(""),
            "getDeviceById(...) should throw IllegalArgumentException when is called with empty id.");
    }

    @Test
    void testGetDeviceByBlankId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.getDeviceById("  "),
            "getDeviceById(...) should throw IllegalArgumentException when is called with blank id.");
    }

    @Test
    void testGetDeviceByIdNotFound() {
        when(storage.exists("SPKR-Device1-0")).thenReturn(false);

        Assertions.assertThrows(DeviceNotFoundException.class, () -> homeCenter.getDeviceById("SPKR-Device1-0"),
            "getDeviceById(...) should throw DeviceNotFoundException when is called with id of device not registered yet.");
    }

    @Test
    void testGetDeviceByCorrectId() throws DeviceNotFoundException {
        String id = "SPKR-Device1-0";
        IoTDevice device = mock();

        when(storage.exists(id)).thenReturn(true);
        when(storage.get(id)).thenReturn(device);

        Assertions.assertDoesNotThrow(() -> homeCenter.getDeviceById(id),
            "getDeviceById(...) should not throw when correct data is passed.");

        Assertions.assertEquals(device, homeCenter.getDeviceById(id),
            "getDeviceById(...) should return device when correct data is passed.");
    }

    @Test
    void testGetDeviceQuantityPerNullType() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.getDeviceQuantityPerType(null),
            "getDeviceQuantityPerType(...) should throw IllegalArgumentException when called with null obj.");
    }

    @Test
    void testGetDeviceQuantityPerCorrectType() {
        IoTDevice device1 = mock();
        when(device1.getType()).thenReturn(DeviceType.SMART_SPEAKER);

        IoTDevice device2 = mock();
        when(device2.getType()).thenReturn(DeviceType.BULB);

        IoTDevice device3 = mock();
        when(device3.getType()).thenReturn(DeviceType.SMART_SPEAKER);

        List<IoTDevice> devices = mock();

        Iterator<IoTDevice> devicesIterator;

        devicesIterator = mock();
        when(devicesIterator.hasNext()).thenReturn(true, true, true, false);
        when(devicesIterator.next()).thenReturn(device1).thenReturn(device2).thenReturn(device3);

        when(devices.iterator()).thenReturn(devicesIterator);

        when(storage.listAll()).thenReturn(devices);

        Assertions.assertEquals(2, homeCenter.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER),
            "getDeviceQuantityPerType(...) should return correct quantity per given type.");
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionNIsZero() {
        Collection<String> expectedArr = Arrays.asList();

        Assertions.assertEquals(expectedArr, homeCenter.getTopNDevicesByPowerConsumption(0),
            "getTopNDevicesByPowerConsumption(...) should return empty array when called with 0.");
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionNIsNegative() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.getTopNDevicesByPowerConsumption(-1),
            "getTopNDevicesByPowerConsumption(..) should throw IllegalArgumentException when is called with negative value of n.");
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionNSmallerThenMax() {
        IoTDevice device1 = mock();
        when(device1.getPowerConsumptionKWh()).thenReturn((long) 100);
        when(device1.getId()).thenReturn("SPKR-Device1-0");

        IoTDevice device2 = mock();
        when(device2.getPowerConsumptionKWh()).thenReturn((long) 101);
        when(device2.getId()).thenReturn("BLB-Device2-1");

        IoTDevice device3 = mock();
        when(device3.getPowerConsumptionKWh()).thenReturn((long) 102);
        when(device3.getId()).thenReturn("SPKR-Device3-2");

        LinkedList<IoTDevice> devices = mock();

        Iterator<IoTDevice> devicesIterator;

        devicesIterator = mock();
        when(devicesIterator.hasNext()).thenReturn(true, true, true, false);
        when(devicesIterator.next()).thenReturn(device1).thenReturn(device2).thenReturn(device3);

        when(devices.iterator()).thenReturn(devicesIterator);

        when(storage.listAll()).thenReturn(devices);

        Collection<String> expectedArr = Arrays.asList("SPKR-Device1-0", "BLB-Device2-1");

        Assertions.assertEquals(expectedArr, homeCenter.getTopNDevicesByPowerConsumption(2),
            "getTopNDevicesByPowerConsumption(...) should return the top N devices with the most power consumption.");
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionNGreaterThenMax() {
        IoTDevice device1 = mock();
        when(device1.getPowerConsumptionKWh()).thenReturn((long) 100);
        when(device1.getId()).thenReturn("SPKR-Device1-0");

        IoTDevice device2 = mock();
        when(device2.getPowerConsumptionKWh()).thenReturn((long) 101);
        when(device2.getId()).thenReturn("BLB-Device2-1");

        IoTDevice device3 = mock();
        when(device3.getPowerConsumptionKWh()).thenReturn((long) 102);
        when(device3.getId()).thenReturn("SPKR-Device3-2");

        LinkedList<IoTDevice> devices = mock();

        Iterator<IoTDevice> devicesIterator;

        devicesIterator = mock();
        when(devicesIterator.hasNext()).thenReturn(true, true, true, false);
        when(devicesIterator.next()).thenReturn(device1).thenReturn(device2).thenReturn(device3);

        when(devices.iterator()).thenReturn(devicesIterator);

        when(storage.listAll()).thenReturn(devices);

        Collection<String> expectedArr = Arrays.asList("SPKR-Device1-0", "BLB-Device2-1", "SPKR-Device3-2");

        Assertions.assertEquals(expectedArr, homeCenter.getTopNDevicesByPowerConsumption(5),
            "getTopNDevicesByPowerConsumption(...) should return all devices with when n is greater then devices count.");
    }

    @Test
    void testGetFirstNDevicesByRegistrationNIsZero() {
        Collection<IoTDevice> expectedArr = Arrays.asList();

        Assertions.assertEquals(expectedArr, homeCenter.getFirstNDevicesByRegistration(0),
            "getFirstNDevicesByRegistration(...) should return empty array when called with 0.");
    }

    @Test
    void testGetFirstNDevicesByRegistrationNIsNegative() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.getFirstNDevicesByRegistration(-1),
            "getFirstNDevicesByRegistration(..) should throw IllegalArgumentException when is called with negative value of n.");
    }

    @Test
    void testGetFirstNDevicesByRegistrationNSmallerThenMax() {
        IoTDevice device1 = mock();
        when(device1.getRegistration()).thenReturn((long) 10);

        IoTDevice device2 = mock();
        when(device2.getRegistration()).thenReturn((long) 11);

        IoTDevice device3 = mock();
        when(device3.getRegistration()).thenReturn((long) 12);

        LinkedList<IoTDevice> devices = mock();

        Iterator<IoTDevice> devicesIterator;

        devicesIterator = mock();
        when(devicesIterator.hasNext()).thenReturn(true, true, true, false);
        when(devicesIterator.next()).thenReturn(device1).thenReturn(device2).thenReturn(device3);

        when(devices.iterator()).thenReturn(devicesIterator);

        when(storage.listAll()).thenReturn(devices);

        Collection<IoTDevice> expectedArr = Arrays.asList(device1, device2);

        Assertions.assertEquals(expectedArr, homeCenter.getFirstNDevicesByRegistration(2),
            "getFirstNDevicesByRegistration(...) should return the first n devices with the most recent registration.");
    }

    @Test
    void testGetFirstNDevicesByRegistrationNGreaterThenMax() {
        IoTDevice device1 = mock();
        when(device1.getRegistration()).thenReturn((long) 10);

        IoTDevice device2 = mock();
        when(device2.getRegistration()).thenReturn((long) 11);

        IoTDevice device3 = mock();
        when(device3.getRegistration()).thenReturn((long) 12);

        LinkedList<IoTDevice> devices = mock();

        Iterator<IoTDevice> devicesIterator;

        devicesIterator = mock();
        when(devicesIterator.hasNext()).thenReturn(true, true, true, false);
        when(devicesIterator.next()).thenReturn(device1).thenReturn(device2).thenReturn(device3);

        when(devices.iterator()).thenReturn(devicesIterator);

        when(storage.listAll()).thenReturn(devices);

        Collection<IoTDevice> expectedArr = Arrays.asList(device1, device2, device3);

        Assertions.assertEquals(expectedArr, homeCenter.getFirstNDevicesByRegistration(5),
            "getFirstNDevicesByRegistration(...) should return all devices when n is greater then devices count.");
    }

}
