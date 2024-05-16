package killercreepr.crux.util;

import killercreepr.crux.registries.Registries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CruxString {
    public static boolean parseBoolean(@NotNull String s){
        for(Map.Entry<String, Boolean> entry : Registries.BOOLEAN_MAPPED.entrySet()){
            if(s.equalsIgnoreCase(entry.getKey())) return entry.getValue();
        }
        return Boolean.parseBoolean(s);
    }

    public static String toTitleCase(@Nullable String string) {
        if(string == null || string.equals("")) return string;
        return Stream.of(string.replaceAll("_", " ").split(" ")).
                map(w -> w.toUpperCase().charAt(0)+ w.toLowerCase().substring(1)).
                reduce((s, s2) -> s + " " + s2).orElse("");
    }

    public static int parseInt(@Nullable String string, int defaultValue){
        if(string == null) return defaultValue;
        try{ return Integer.parseInt(string); } catch (IllegalArgumentException ignored){ return defaultValue; }
    }

    public static @NotNull String[] quoteSplit(@NotNull String input, @NotNull String delimiter) {
        List<String> result = new ArrayList<>();

        // Escape the delimiter for regex if it's a special character
        String regexDelimiter = Pattern.quote(delimiter);

        Pattern pattern = Pattern.compile("[^\\s" + regexDelimiter + "\"]+|\"([^\"]*)\"|'([^']*)'");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Double-quoted string
                result.add(matcher.group(1));
            } else if (matcher.group(2) != null) {
                // Single-quoted string
                result.add(matcher.group(2));
            } else {
                // Unquoted string
                result.add(matcher.group());
            }
        }

        return result.toArray(new String[0]);
    }
}
