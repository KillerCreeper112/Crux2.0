package killercreepr.crux.tags.hook;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.format.FormatArgs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface HookTag<I, O> {
    @NotNull Class<I> getObjectType();
    @Nullable
    O resolve(@NotNull I input, @NotNull FormatArgs args, @NotNull TextParserContext context);
}
