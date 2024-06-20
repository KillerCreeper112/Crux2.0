package killercreepr.crux.tags;

import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.ObjectTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TagsPrefixBuilder {
    <T> @Nullable FormatPrefix buildPrefix(@NotNull ObjectTag<T> tag, @NotNull T object, @Nullable TagContainer<?> tags);
    default <T> @Nullable FormatPrefix buildHookedPrefix(@NotNull ObjectTag<T> tag, @NotNull T object, @Nullable TagContainer<?> tags){
        return buildPrefix(tag, object, tags);
    }
}
