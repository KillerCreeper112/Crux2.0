package killercreepr.cruxconfig.config.bukkit.loader;

import killercreepr.crux.Crux;
import killercreepr.crux.data.tag.block.BlockTag;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class BlockTagLoader extends CfgLoader {
    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        BlockTag table;
        if(path == null) table = cfg.deserialize("", BlockTag.class);
        else{
            if(!(cfg.getRoot() instanceof FileObject root)) return;

            table = BukkitCfgHandlers.BLOCK_TAG.deserializeFromFile(
                new FileContext<>(cfg.fileRegistry()), root, Crux.key(path)
            );
        }
        if(table == null) return;
        Crux.log(Level.INFO, "Registered block tag: " + table.key());
        CruxRegistries.BLOCK_TAG.register(table);
    }
}
