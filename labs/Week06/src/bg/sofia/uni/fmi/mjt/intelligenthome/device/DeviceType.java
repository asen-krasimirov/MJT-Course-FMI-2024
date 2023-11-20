package bg.sofia.uni.fmi.mjt.intelligenthome.device;

public enum DeviceType {

    SMART_SPEAKER("SPKR"), BULB("BLB"), THERMOSTAT("TMST");

    private String shortName = "";

    private DeviceType(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

}
