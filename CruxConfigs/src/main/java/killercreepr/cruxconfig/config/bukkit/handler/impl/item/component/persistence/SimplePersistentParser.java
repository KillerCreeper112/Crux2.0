package killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.persistence;

import killercreepr.crux.api.item.dynamic.component.persistence.TypedDynamicPersistentTag;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SimplePersistentParser<T> extends FileDynamicPersistTagParser<T>{
    @Override
    default @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull TypedDynamicPersistentTag<?> object){
        FileObject o = new FileObject();
        o.addProperty("key", object.getTagKey());
        Object value = object.getValue();
        if(value == null) return o;
        FileElement e = serializeTypedValue(ctx, value);
        if(e==null) return o;
        o.add("value", e);
        return o;
    }

    @Nullable FileElement serializeTypedValue(@NotNull FileContext<?> ctx, @NotNull Object object);

    default @Nullable String defaultTagKey(){
        return null;
    }

    default @Nullable
    TypedDynamicPersistentTag<?> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e){
        if(!(e instanceof FileObject o)) return null;
        String tagKey = o.getObject(String.class, "key");
        if(tagKey == null) tagKey = defaultTagKey();
        if(tagKey==null) return null;
        FileElement value = o.get("value");
        if(value == null) return null;
        return TypedDynamicPersistentTag.typedUnchecked(
             getDynamicTag(ctx, e), tagKey, parseObject(ctx, o, value)
        );
    }
}
