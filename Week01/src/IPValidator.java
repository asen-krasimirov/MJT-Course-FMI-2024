public class IPValidator {
    public static boolean isNumberValid(char[] number) {
        if (number[0] == '0' && number.length != 1) return false;

        int value = 0;

        for (int i = number.length - 1, multiplier = 0; i >= 0; --i, ++multiplier) {
            if ('0' > number[i] || number[i] > '9') return false;
            value += (int) ((number[i] - '0') * Math.pow(10, multiplier));
        }

        if (0 > value || value > 255) return false;

        return true;
    }
    public static boolean validateIPv4Address(String str) {
        String[] tokens = str.split("\\.");
        if (tokens.length != 4) return false;

        for (String token: tokens) {
            if (token.isEmpty() || token.isBlank()) return false;
            if (!isNumberValid(token.toCharArray())) return false;
        }

        return true;
    }
}
