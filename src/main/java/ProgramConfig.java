import java.time.format.DateTimeFormatter;

public class ProgramConfig {

    public static final String SAVE_FILE_NAME = "reminders.json";

    public static final String DATE_FORMAT_PATTERN = "d/M/yyyy";
    public static final String TIME_FORMAT_PATTERN = "HH:mm:ss";

    public static DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
    }

    public static DateTimeFormatter getTimeFormatter() {
        return DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN);
    }

    public static DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN + " " + TIME_FORMAT_PATTERN);
    }
}
