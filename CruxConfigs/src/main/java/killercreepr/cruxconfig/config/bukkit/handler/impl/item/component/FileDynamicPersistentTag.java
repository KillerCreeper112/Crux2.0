package killercreepr.cruxconfig.config.bukkit.handler.impl.item.component;

import killercreepr.crux.item.dynamic.components.DynamicPersistentTag;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileDynamicPersistentTag implements FileHandler<DynamicPersistentTag> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull DynamicPersistentTag object) {
        FileRegistry registry = ctx.getRegistry();
        return new FileObject()
            .addProperty("type", object.getType())
            .add("key", registry.serializeToFile(object.getKey()))
            .add("value", registry.serializeToFile(object.getValue()))
            ;
    }

    @Override
    public @Nullable DynamicPersistentTag deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileElement key = o.get("key");
        return deserialize(ctx, e, key == null ? null : key.getAsString());
    }

    public static @Nullable DynamicPersistentTag deserialize(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable Object key) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        FileElement value = o.get("value");
        if(value == null) return null;
        return new DynamicPersistentTag(type, key, value.getAsString());
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "dynamic_persistent_tag";
    }
}
