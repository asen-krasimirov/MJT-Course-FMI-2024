package bg.sofia.uni.fmi.mjt.spotify.validator;

public class EmailValidator {
    public static boolean isValidEmail(String email) {
        if (!email.contains("@")) {
            return false;
        }

        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }

        String localPart = parts[0];
        String domainPart = parts[1];

        if (localPart.isEmpty()) {
            return false;
        } else if (!domainPart.contains(".")) {
            return false;
        } else if (domainPart.startsWith(".") || domainPart.endsWith(".")) {
            return false;
        } else return !domainPart.contains("..");
    }
}
