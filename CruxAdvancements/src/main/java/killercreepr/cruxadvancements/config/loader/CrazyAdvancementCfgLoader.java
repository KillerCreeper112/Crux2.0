package killercreepr.cruxadvancements.config.loader;

import killercreepr.crux.Crux;
import killercreepr.cruxadvancements.crazy.CrazyAdvancement;
import killercreepr.cruxadvancements.crazy.CrazyAdvancementManager;
import killercreepr.cruxadvancements.crazy.CrazyAdvancementsHook;
import killercreepr.cruxconfig.config.bukkit.loader.CfgLoader;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class CrazyAdvancementCfgLoader extends CfgLoader {
    protected final @NotNull CrazyAdvancementManager<CrazyAdvancement> manager;
    public CrazyAdvancementCfgLoader(@NotNull CrazyAdvancementManager<CrazyAdvancement> manager) {
        this.manager = manager;
    }

    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        CrazyAdvancement table;
        if(path == null) table = cfg.deserialize("", CrazyAdvancement.class);
        else{
            if(!(cfg.getRoot() instanceof FileObject root)) return;

            FileContext<?> ctx = new FileContext<>(cfg.fileRegistry());
            table = CrazyAdvancementsHook.FILE_CRAZY_ADVANCEMENT.deserializeFromFile(
                ctx, root, Crux.key(path)
            );

            if(table != null) table = cfg.fileRegistry().getParsedObjectRegistry().parse(root, ctx, table);
        }
        if(table == null) return;
        manager.registerAdvancement(table);
        Crux.log(Level.INFO, "Registered crazy crux advancement: " + manager.key() + " -> " + table.key());
    }
}
