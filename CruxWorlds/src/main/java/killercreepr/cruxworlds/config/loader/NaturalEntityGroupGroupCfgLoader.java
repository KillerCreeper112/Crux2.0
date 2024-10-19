package killercreepr.cruxworlds.config.loader;

import killercreepr.crux.Crux;
import killercreepr.cruxconfig.config.bukkit.loader.CfgLoader;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxworlds.config.CruxConfigsHook;
import killercreepr.cruxworlds.registries.WorldsRegistries;
import killercreepr.cruxworlds.world.entity.KeyedNaturalEntitySpawnGroup;
import killercreepr.cruxworlds.world.entity.NaturalEntitySpawnGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class NaturalEntityGroupGroupCfgLoader extends CfgLoader {
    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path) {
        NaturalEntitySpawnGroup table;
        if(path == null) table = cfg.deserialize("", NaturalEntitySpawnGroup.class);
        else{
            if(!(cfg.getRoot() instanceof FileObject root)) return;

            FileContext<?> ctx = new FileContext<>(cfg.fileRegistry());
            table = CruxConfigsHook.NATURAL_ENTITY_SPAWN_GROUP.deserializeFromFile(
                ctx, root, Crux.key(path)
            );
            if(table != null) table = cfg.fileRegistry().getParsedObjectRegistry().parse(root, ctx, table);
        }
        if(!(table instanceof KeyedNaturalEntitySpawnGroup keyed)) return;
        Crux.log(Level.INFO, "Registered entity spawn group: " + keyed.key());
        WorldsRegistries.NATURAL_ENTITY_SPAWN_GROUP.register(keyed);
    }
}
