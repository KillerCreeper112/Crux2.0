package killercreepr.crux.tags.standard;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.format.FormatArgs;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.util.CruxString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class DurationFormatResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "duration";
    }

    //<duration:1000:<excluded>:false:", ":"%d days":"%h hours":"%m minutes":"%s seconds">
    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext ctx) {
        long milliseconds = (long) Double.parseDouble(ctx.deserializeString(args.get(0)));
        Collection<String> excluded;
        if(args.has(1)){
            excluded = Set.of(args.get(1).split(","));
        }else excluded = Set.of();

        boolean displayZero = args.has(2) && CruxString.parseBoolean(args.get(2));

        String separator = args.getOrDefault(3, ", ");
        List<String> format = args.has(4) ? buildFormat(args, 4) : List.of("%dd", "%hh", "%mm", "%ss");

        return format(milliseconds, separator, format, excluded, displayZero);
    }

    public @NotNull List<String> buildFormat(@NotNull FormatArgs args, int start){
        List<String> list = new ArrayList<>();
        while(start < args.size()){
            String arg = args.get(start);
            list.add(arg);
            start++;
        }
        return list;
    }

    public static  boolean hasSeconds(@NotNull Collection<String> excluded){
        return excluded.contains("second") || excluded.contains("sec") || excluded.contains("s");
    }

    public static  boolean hasMinutes(@NotNull Collection<String> excluded){
        return excluded.contains("minute") || excluded.contains("min") || excluded.contains("m");
    }

    public static  boolean hasHours(@NotNull Collection<String> excluded){
        return excluded.contains("hour") || excluded.contains("h");
    }

    public static  boolean hasDays(@NotNull Collection<String> excluded){
        return excluded.contains("day") || excluded.contains("d");
    }

    //Parameters:
    //1: time in milliseconds
    //2: Format
    //3 and beyond...: Excluded parameters
    //<duration:1000:%d, %h, %m, %s:days,hours>
    public static @NotNull String format(long milliseconds,
                                         @NotNull String separator,
                                         @NotNull List<String> format,
                                         @NotNull Collection<String> excluded,
                                         boolean displayZero){
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        //<duration:1000:<excluded>:", ":"%d days":"%h hours":"%m minutes":"%s seconds">

        if (!hasSeconds(excluded)) seconds %= 60;
        if (!hasMinutes(excluded)) minutes %= 60;
        if (!hasHours(excluded)) hours %= 24;

        StringBuilder result = new StringBuilder();

        // Only append each time unit if it's non-zero and not excluded
        if (!excluded.contains("days") && (displayZero || days > 0)) {
            if(!result.isEmpty()) result.append(separator);
            result.append(format.get(0).replace("%d", days + ""));
        }
        if (!excluded.contains("hours") && (displayZero || hours > 0)) {
            if(!result.isEmpty()) result.append(separator);
            result.append(format.get(1).replace("%h", hours + ""));
        }
        if (!excluded.contains("minutes") && (displayZero || minutes > 0)) {
            if(!result.isEmpty()) result.append(separator);
            result.append(format.get(2).replace("%m", minutes + ""));
        }
        if (!excluded.contains("seconds") && (displayZero || seconds > 0)) {
            if(!result.isEmpty()) result.append(separator);
            result.append(format.get(3).replace("%s", seconds + ""));
        }

        // Trim any trailing whitespace and return the result
        return result.toString().trim();
    }
}
