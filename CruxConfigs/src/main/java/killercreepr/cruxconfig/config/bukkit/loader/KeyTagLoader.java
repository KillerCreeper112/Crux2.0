package killercreepr.cruxconfig.config.bukkit.loader;

import killercreepr.crux.api.key.tag.KeyTag;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class KeyTagLoader extends CfgLoader {
    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        KeyTag table;
        if(path == null) table = cfg.deserialize("", KeyTag.class);
        else{
            if(!(cfg.getRoot() instanceof FileObject root)) return;

            table = BukkitCfgHandlers.KEY_TAG.deserializeFromFile(
                new FileContext<>(cfg.fileRegistry()), root, Crux.key(path)
            );
        }
        if(table == null) return;
        Crux.log(Level.INFO, "Registered key tag: " + table.key());
        CruxRegistries.KEY_TAG.register(table);
    }
}
