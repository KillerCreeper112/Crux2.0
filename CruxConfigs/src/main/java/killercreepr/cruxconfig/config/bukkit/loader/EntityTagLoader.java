package killercreepr.cruxconfig.config.bukkit.loader;

import killercreepr.crux.core.Crux;
import killercreepr.crux.api.entity.tag.EntityTag;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class EntityTagLoader extends CfgLoader {
    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        EntityTag table;
        if(path == null) table = cfg.deserialize("", EntityTag.class);
        else{
            if(!(cfg.getRoot() instanceof FileObject root)) return;

            table = BukkitCfgHandlers.ENTITY_TAG.deserializeFromFile(
                new FileContext<>(cfg.fileRegistry()), root, Crux.key(path)
            );
        }
        if(table == null) return;
        Crux.log(Level.INFO, "Registered item tag: " + table.key());
        CruxRegistries.ENTITY_TAG.register(table);
    }
}
