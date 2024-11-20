package killercreepr.crux.api.text.resolver;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.text.format.FormatArgs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TagResolver<T> {
    @NotNull String identifier();
    @Nullable T resolve(@NotNull FormatArgs args, @NotNull TextParserContext context);
}
