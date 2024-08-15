package killercreepr.cruxitems.config.handler;

import killercreepr.crux.item.dynamic.BukkitDynamicItem;
import killercreepr.crux.item.dynamic.DynamicItem;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxitems.item.plugin.CfgPluginItem;
import killercreepr.cruxitems.item.plugin.PluginItem;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FilePluginItem implements FileHandler<PluginItem> {
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

    public static @Nullable PluginItem deserialize(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull Key key) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        DynamicItem dynamic = registry.deserializeFromFile(BukkitDynamicItem.class, o.get("item"));
        if(dynamic==null) return null;
        return new CfgPluginItem(key, dynamic);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "plugin_item";
    }
}
