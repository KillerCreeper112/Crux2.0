package killercreepr.crux.tags.resolver;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.format.FormatArgs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TagResolver<T> {
    @NotNull String identifier();
    @Nullable T resolve(@NotNull FormatArgs args, @NotNull TextParserContext context);
}
