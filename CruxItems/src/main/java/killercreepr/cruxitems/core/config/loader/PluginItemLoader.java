package killercreepr.cruxitems.core.config.loader;

import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.loader.CfgLoader;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxitems.api.item.plugin.PluginItem;
import killercreepr.cruxitems.core.CruxItemsModule;
import killercreepr.cruxitems.core.config.Config;
import killercreepr.cruxitems.core.config.CruxItemsConfigHook;
import killercreepr.cruxitems.core.item.CfgItemType;
import killercreepr.cruxitems.core.item.plugin.CfgPluginItem;
import killercreepr.cruxitems.core.registries.CruxItemRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
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

        if(cfg.getRoot() instanceof FileObject o){
            CfgItemType cfgItemType = cfg.fileRegistry().deserializeFromFile(CfgItemType.class, o.get("crux_default_item_type"));
            if(cfgItemType != null && CruxRegistries.MODULES.get(StandardModules.CRUX_ITEMS) instanceof CruxItemsModule module){
                if(module.values() instanceof Config cf){
                    if(cf.ITEM_TYPES.value() == null) cf.ITEM_TYPES.setValue(new HashMap<>());
                    cf.ITEM_TYPES.value().put(table.key(), cfgItemType);
                }
            }
        }

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

            if(value instanceof FileObject o){
                CfgItemType cfgItemType = ctx.getRegistry().deserializeFromFile(CfgItemType.class, o.get("crux_default_item_type"));
                if(cfgItemType != null && CruxRegistries.MODULES.get(StandardModules.CRUX_ITEMS) instanceof CruxItemsModule module){
                    if(module.values() instanceof Config cfg){
                        if(cfg.ITEM_TYPES.value() == null) cfg.ITEM_TYPES.setValue(new HashMap<>());
                        cfg.ITEM_TYPES.value().put(item.key(), cfgItemType);
                    }
                }
            }

            CruxItemRegistries.ITEMS.register(item);
            Crux.log(Level.INFO, "Registered plugin item: " + item.key());
        });
    }
}
