package killercreepr.crux.tags.ab.container;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.data.DataExchange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TagContainer<T> extends Iterable<T> {
    default TagContainer<T> hookAll(@NotNull DataExchange info){
        info.forEach(this::hook);
        return this;
    }
    default TagContainer<T> hookAll(@NotNull TextParserContext context, @NotNull DataExchange info){
        info.forEach(e -> hook(context, e));
        return this;
    }
    TagContainer<T> hook(@Nullable Object info);
    TagContainer<T> hook(@NotNull TextParserContext context, @Nullable Object info);

    TagContainer<T> add(@NotNull T resolver);
}
