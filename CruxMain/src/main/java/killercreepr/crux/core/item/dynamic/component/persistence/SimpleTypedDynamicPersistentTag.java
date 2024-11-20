package killercreepr.crux.core.item.dynamic.component.persistence;

import killercreepr.crux.api.item.dynamic.component.persistence.DynamicPersistentTag;
import killercreepr.crux.api.item.dynamic.component.persistence.TypedDynamicPersistentTag;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.util.CruxTag;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleTypedDynamicPersistentTag<E> implements TypedDynamicPersistentTag<E> {
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

    @Override
    public @NotNull DynamicPersistentTag<E, ?> getTag() {
        return tag;
    }

    @Override
    @Nullable
    public E getValue() {
        return value;
    }
}
