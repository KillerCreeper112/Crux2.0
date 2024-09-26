package killercreepr.crux.tags.standard;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.format.FormatArgs;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.util.CruxString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "date";
    }

    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext ctx) {
        long milliseconds = Long.parseLong(ctx.deserializeString(args.get(0)));
        String format;
        if(args.has(1)){
            format = args.get(1);
        }else{
            format = "EEEE, MMMM dd";
        }
        return format(new Date(milliseconds), format);
    }

    public static @NotNull String format(@NotNull Date date, @NotNull String format){
        return new SimpleDateFormat(format).format(date);
    }

    public static @NotNull String resolve(@NotNull String text){
        return CruxString.latinFont(text);
    }
}
