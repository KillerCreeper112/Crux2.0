package killercreepr.crux.item.dynamic.components.persistence;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxTag;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleTypedDynamicPersistentTag<E> implements TypedDynamicPersistentTag {
    protected final @NotNull DynamicPersistentTag<E, ?> tag;
    protected final @NotNull String tagKey;
    protected final @Nullable E value;

    public SimpleTypedDynamicPersistentTag(@NotNull DynamicPersistentTag<E, ?> tag, @NotNull String tagKey, @Nullable E value) {
        this.tag = tag;
        this.tagKey = tagKey;
        this.value = value;
    }

    @Override
    public <T extends PersistentDataContainer> T apply(@NotNull T to, @NotNull TextParserContext ctx) {
        if(value == null){
            return CruxTag.remove(to, tagKey);
        }
        return tag.apply(to, ctx, tagKey, value);
    }

    @Override
    public @NotNull String getTagKey() {
        return tagKey;
    }
}
