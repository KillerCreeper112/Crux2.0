package killercreepr.crux.core.util;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class CruxTimeUtil {
    public static final Parser nattyParser = new Parser();
    public static Instant parseInstant(String text){
        try {
            return Instant.parse(text);
        } catch (Exception ignored) {}

        List<DateGroup> groups = nattyParser.parse(text);
        if (!groups.isEmpty() && !groups.getFirst().getDates().isEmpty()) {
            Date date = groups.getFirst().getDates().getFirst();
            return date.toInstant();
        }
        throw new IllegalArgumentException("Could not parse date/time " + text);
    }

    public static final ZoneId ZONE_EST = ZoneId.of("America/New_York");
    public static String generalFormatInstant(Instant instant, ZoneId zone) {
        ZonedDateTime zdt = instant.atZone(zone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd yyyy, HH:mm z");
        return zdt.format(formatter);
    }

    public static String generalFormatInstant(Instant instant){
        return generalFormatInstant(instant, ZoneId.systemDefault());
    }

    public static String generalFormatInstantEST(Instant instant) {
        return generalFormatInstant(instant, ZONE_EST);
    }
}
