package killercreepr.cruxconfig.config.bukkit.handler.impl.item.component;

import killercreepr.crux.item.dynamic.components.persistence.TypedDynamicPersistentTag;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.persistence.FileDynamicPersistTagParser;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileDynamicPersistentTag implements FileObjectHandler<TypedDynamicPersistentTag<?>> {
/*    public final KeyedRegistry<FileDynamicPersistTagParser> TYPE_HANDLERS = new SimpleKeyedRegistry<>();
    public KeyedRegistry<FileDynamicPersistTagParser> typeHandlers(){
        return TYPE_HANDLERS;
    }*/
/*    public static FileObject buildBase(@NotNull FileContext<?> ctx, @NotNull TypedDynamicPersistentTag tag){
        return new FileObject()
            .add("persistent_type", ctx.getRegistry().serializeToFile(tag.key()))
            .addProperty("key", tag.getTagKey())
            ;
    }*/

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull TypedDynamicPersistentTag<?> object) {
   /*     FileDynamicPersistTagParser handler = TYPE_HANDLERS.get(object.key());
        if(handler==null) throw new RuntimeException("No type handler found for " + object.key() + "!");
        return handler.serializeToFile(ctx, object);*/
        //todo
        return null;
    }

    @Override
    public @Nullable TypedDynamicPersistentTag<?> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        Key type = ctx.getRegistry().deserializeFromFile(Key.class, o.get("persistent_type"));
        if(type==null) return null;
        FileDynamicPersistTagParser<?> parser = CfgRegistries.DYNAMIC_PERSIST_TAG_PARSER.get(type);
        if(parser==null) throw new RuntimeException("No type handler found for " + type + "!");
        return parser.deserializeFromFile(ctx, e);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "dynamic_persistent_tag";
    }
}
