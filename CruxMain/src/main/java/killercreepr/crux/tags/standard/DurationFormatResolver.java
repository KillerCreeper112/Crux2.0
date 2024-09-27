package killercreepr.crux.tags.standard;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.format.FormatArgs;
import killercreepr.crux.tags.resolver.StringResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DurationFormatResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "duration";
    }

    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext ctx) {
        long milliseconds = (long) Double.parseDouble(ctx.deserializeString(args.get(0)));
        String format = args.has(1) ? args.get(1) : "%dd, %hh, %mm, %ss";
        Collection<String> excluded;
        if(args.has(2)){
            excluded = Set.of(args.get(2).split(","));
        }else excluded = Set.of();
        return format(milliseconds, format, excluded);
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
    public static @NotNull String format(long milliseconds, @NotNull String format, @NotNull Collection<String> excluded){
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if(!hasSeconds(excluded)) seconds %= 60;
        if(!hasMinutes(excluded)) minutes %= 60;
        if(!hasHours(excluded)) hours %= 24;

        return format.replace("%d", days == 0 ? "" : String.valueOf(days))
            .replace("%h", hours == 0 ? "" : String.valueOf(hours))
            .replace("%m", minutes == 0 ? "" : String.valueOf(minutes))
            .replace("%s", seconds == 0 ? "" : String.valueOf(seconds));
    }
}
