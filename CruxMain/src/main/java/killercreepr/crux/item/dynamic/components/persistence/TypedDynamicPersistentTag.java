package killercreepr.crux.item.dynamic.components.persistence;

import killercreepr.crux.context.TextParserContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TypedDynamicPersistentTag {
    static <E> TypedDynamicPersistentTag typed(@NotNull DynamicPersistentTag<E, ?> tag, @NotNull String tagKey, @Nullable E value){
        return new SimpleTypedDynamicPersistentTag<>(tag, tagKey, value);
    }

    static <E> TypedDynamicPersistentTag typedUnchecked(@NotNull DynamicPersistentTag<E, ?> tag, @NotNull String tagKey, @Nullable Object value){
        return new SimpleTypedDynamicPersistentTag<>(tag, tagKey, (E) value);
    }

    default <T extends PersistentDataHolder> T apply(@NotNull T to, @NotNull TextParserContext ctx){
        apply(to.getPersistentDataContainer(), ctx);
        return to;
    }
    <T extends PersistentDataContainer> T apply(@NotNull T to, @NotNull TextParserContext ctx);
    @NotNull
    String getTagKey();
}
