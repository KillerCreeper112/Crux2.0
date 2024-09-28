package killercreepr.crux.tags.standard;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.format.FormatArgs;
import killercreepr.crux.tags.resolver.StringResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CurrentTimeResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "current_time";
    }

    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
        return System.currentTimeMillis() + "";
    }
}
