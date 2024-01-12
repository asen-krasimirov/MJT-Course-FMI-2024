package bg.sofia.uni.fmi.mjt.space.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateParser {
    private static final DateTimeFormatter DATA_FORMAT =
        DateTimeFormatter.ofPattern("EEE MMM dd, yyyy", Locale.ENGLISH);

    public static LocalDate parseDate(String dataToParse) {
        return LocalDate.parse(dataToParse, DATA_FORMAT);
    }
}
