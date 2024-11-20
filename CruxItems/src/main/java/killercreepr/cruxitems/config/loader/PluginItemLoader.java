package killercreepr.cruxitems.config.loader;

import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.bukkit.loader.CfgLoader;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxitems.config.CruxItemsConfigHook;
import killercreepr.cruxitems.item.plugin.CfgPluginItem;
import killercreepr.cruxitems.item.plugin.PluginItem;
import killercreepr.cruxitems.registries.CruxItemRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class PluginItemLoader extends CfgLoader {
    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        if(cfg.getElement("values") instanceof FileObject values){
            loadMultipleValues(new FileContext<>(cfg.fileRegistry()), values);
            return;
        }

        PluginItem table;
        if(path == null) table = cfg.deserialize("", CfgPluginItem.class);
        else{
            if(!(cfg.getRoot() instanceof FileObject root)) return;

            FileContext<?> ctx = new FileContext<>(cfg.fileRegistry());
            table = CruxItemsConfigHook.filePluginItem().deserialize(
                ctx, root, Crux.key(path)
            );
            if(table != null) table = ctx.getRegistry().getParsedObjectRegistry().parse(root, ctx, table);
        }
        if(table == null) return;
        CruxItemRegistries.ITEMS.register(table);
        Crux.log(Level.INFO, "Registered plugin item: " + table.key());
    }

    public void loadMultipleValues(@NotNull FileContext<?> ctx, @NotNull FileObject values){
        values.forEach((key, value) ->{
            PluginItem item = CruxItemsConfigHook.filePluginItem().deserialize(
                ctx, value, Crux.key(key)
            );
            if(item != null) item = ctx.getRegistry().getParsedObjectRegistry().parse(value, ctx, item);
            if(item == null) return;
            CruxItemRegistries.ITEMS.register(item);
            Crux.log(Level.INFO, "Registered plugin item: " + item.key());
        });
    }
}
