package killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.persistence;

import killercreepr.crux.item.dynamic.components.persistence.TypedDynamicPersistentTag;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SimplePersistentParser<T> extends FileDynamicPersistTagParser<T>{
    default @Nullable
    TypedDynamicPersistentTag deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e){
        if(!(e instanceof FileObject o)) return null;
        String tagKey = o.getObject(String.class, "key");
        if(tagKey==null) return null;
        FileElement value = o.get("value");
        if(value == null) return null;
        return TypedDynamicPersistentTag.typedUnchecked(
             getDynamicTag(ctx, e), tagKey, parseObject(ctx, o, value)
        );
    }
}
