package killercreepr.cruxitems.config.handler;

import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.core.registry.SimpleKeyedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxitems.item.plugin.CfgPluginItem;
import killercreepr.cruxitems.item.plugin.PluginItem;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FilePluginItem implements FileObjectHandler<PluginItem> {
    public final KeyedRegistry<FilePluginItemHandler> ITEM_TYPES = new SimpleKeyedRegistry<>();

    public void registerTypeHandler(@NotNull FilePluginItemHandler... handlers){
        for(FilePluginItemHandler h : handlers){
            registerTypeHandler(h);
        }
    }

    public void registerTypeHandler(@NotNull FilePluginItemHandler handler){
        ITEM_TYPES.register(handler);
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull PluginItem object) {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public @Nullable PluginItem deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Key key = registry.deserializeFromFile(Key.class, o.get("key"));
        if(key==null) return null;
        return deserialize(ctx, e, key);
    }

    public @Nullable PluginItem deserialize(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull Key key) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        DynamicItem dynamic = registry.deserializeFromFile(DynamicItem.class, o.get("item"));
        if(dynamic==null) return null;

        Key type = registry.deserializeFromFile(Key.class, o.get("type"));
        if(type!= null){
            FilePluginItemHandler handler = ITEM_TYPES.get(type);
            if(handler != null) return handler.deserialize(ctx, e, key, dynamic);
        }

        return new CfgPluginItem(key, dynamic);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "plugin_item";
    }
}
