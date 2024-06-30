package killercreepr.crux.tags.standard;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.format.FormatArgs;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.util.CruxString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatinFontResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "latinfont";
    }

    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
        return resolve(args.get(0));
    }

    public static @NotNull String resolve(@NotNull String text){
        return CruxString.latinFont(text);
    }
}
