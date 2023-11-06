package bg.sofia.uni.fmi.mjt.gym.member;

public record Address(double longitude, double latitude) {

    public double getDistanceTo(Address other) {
        double longitudeDiff = other.longitude - this.longitude;
        double latitudeDiff = other.latitude - this.latitude;

        return Math.sqrt(Math.pow(longitudeDiff, 2) + Math.pow(latitudeDiff, 2));
    }
}
