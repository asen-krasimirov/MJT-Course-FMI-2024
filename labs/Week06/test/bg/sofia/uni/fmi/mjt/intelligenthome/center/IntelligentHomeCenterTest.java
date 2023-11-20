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

import java.util.Iterator;
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
        when(devices.get(0)).thenReturn(device1);
        when(devices.get(1)).thenReturn(device2);
        when(devices.get(2)).thenReturn(device3);

        Iterator<IoTDevice> devicesIterator;

        devicesIterator = mock();
        when(devicesIterator.hasNext()).thenReturn(true, true, true, false);
        when(devicesIterator.next()).thenReturn(device1).thenReturn(device2).thenReturn(device3);

        when(devices.iterator()).thenReturn(devicesIterator);

        when(storage.listAll()).thenReturn(devices);

        Assertions.assertEquals(2, homeCenter.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER),
            "getDeviceQuantityPerType(...) should return correct quantity per given type.");
    }

}
