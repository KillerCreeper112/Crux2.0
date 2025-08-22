package killercreepr.crux.core.util;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.time.*;
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
}
