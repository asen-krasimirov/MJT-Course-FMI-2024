package bg.sofia.uni.fmi.mjt.simcity.constants;

public class Constants {

    public static final double ROUND_UP_NUMBER = 100.0;

    public static double roundUpNumber(double number) {
        return Math.round(number * ROUND_UP_NUMBER) / ROUND_UP_NUMBER;
    }

}
