package killercreepr.cruxconfig.config.bukkit.loader;

import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class TypedDataComponentLoader extends CfgLoader {
    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        Key key;
        TypedDataComponent<?> table;
        if(path == null){
            table = cfg.deserialize("", TypedDataComponent.class);
            key = null;
        }
        else{
            if(!(cfg.getRoot() instanceof FileObject root)) return;
            key = Crux.key(path);
            table = cfg.fileRegistry().deserializeFromFile(TypedDataComponent.class, root);
            /*table = BukkitCfgHandlers.ITEM_LOOT_TABLE.deserializeFromFile(
                new FileContext<>(cfg.fileRegistry()), root, Crux.key(path)
            );*/
        }
        if(table == null || key == null) return;
        Crux.log(Level.INFO, "Registered typed data component: " + key);
        CruxRegistries.TYPED_DATA_COMPONENTS.register(table);
    }
}
