package killercreepr.crux.api.item.dynamic.component.persistence;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.item.dynamic.component.persistence.SimpleTypedDynamicPersistentTag;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TypedDynamicPersistentTag<E> {
    static <E> TypedDynamicPersistentTag<E> typed(@NotNull DynamicPersistentTag<E, ?> tag, @NotNull String tagKey, @Nullable E value){
        return new SimpleTypedDynamicPersistentTag<>(tag, tagKey, value);
    }

    static <E> TypedDynamicPersistentTag<E> typedUnchecked(@NotNull DynamicPersistentTag<E, ?> tag, @NotNull String tagKey, @Nullable Object value){
        return new SimpleTypedDynamicPersistentTag<>(tag, tagKey, (E) value);
    }

    default <T extends PersistentDataHolder> T apply(@NotNull T to, @NotNull TextParserContext ctx){
        apply(to.getPersistentDataContainer(), ctx);
        return to;
    }
    <T extends PersistentDataContainer> T apply(@NotNull T to, @NotNull TextParserContext ctx);
    @NotNull
    String getTagKey();
    @NotNull DynamicPersistentTag<E, ?> getTag();
    @Nullable E getValue();
}
