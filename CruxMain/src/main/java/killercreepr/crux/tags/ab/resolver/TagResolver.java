package killercreepr.crux.tags.ab.resolver;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.FormatArgs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TagResolver<T> {
    @NotNull String identifier();
    /*default boolean has(final @NotNull String name, final @Nullable FormatPrefix prefix){
        String pre = prefix==null?"":prefix.buildPrefix(this);
        return name.equalsIgnoreCase(pre + identifier());
    }*/
    @Nullable T resolve(@NotNull FormatArgs args, @NotNull TextParserContext context);
}
