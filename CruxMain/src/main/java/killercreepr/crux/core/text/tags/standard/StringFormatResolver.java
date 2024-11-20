package killercreepr.crux.core.text.tags.standard;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.core.text.format.FormatArgs;
import killercreepr.crux.core.util.CruxString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringFormatResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "strformat";
    }

    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
        String text = args.get(0);
        return resolve(text, args.getOrDefault(1, "title").toLowerCase());
    }

    public static @NotNull String resolve(@NotNull String text, @NotNull String formatType){
        switch (formatType){
            case "title" -> text = CruxString.toTitleCase(text);
            case "lower" -> text = text.toLowerCase();
            case "upper" -> text = text.toUpperCase();
        }
        return text;
    }
}
