package killercreepr.cruxconfig.config.bukkit.loader;

import killercreepr.crux.Crux;
import killercreepr.crux.data.tag.item.ItemTag;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class ItemTagLoader extends CfgLoader {
    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        ItemTag table;
        if(path == null) table = cfg.deserialize("", ItemTag.class);
        else{
            if(!(cfg.getRoot() instanceof FileObject root)) return;

            table = BukkitCfgHandlers.ITEM_TAG.deserializeFromFile(
                new FileContext<>(cfg.fileRegistry()), root, Crux.key(path)
            );
        }
        if(table == null) return;
        Crux.log(Level.INFO, "Registered item tag: " + table.key());
        CruxRegistries.ITEM_TAG.register(table);
    }
}
