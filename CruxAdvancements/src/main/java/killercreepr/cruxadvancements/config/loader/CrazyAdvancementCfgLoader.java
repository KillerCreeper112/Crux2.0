package killercreepr.cruxadvancements.config.loader;

import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxadvancements.crazy.CrazyAdvancement;
import killercreepr.cruxadvancements.crazy.CrazyAdvancementsHook;
import killercreepr.cruxconfig.config.bukkit.loader.CfgLoader;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CrazyAdvancementCfgLoader extends CfgLoader {
    protected final CruxPlugin plugin;
    protected final Consumer<CrazyAdvancement> loaded;
    public CrazyAdvancementCfgLoader(CruxPlugin plugin, Consumer<CrazyAdvancement> loaded) {
        this.plugin = plugin;
        this.loaded = loaded;
    }

    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        CrazyAdvancement table;
        if(path == null) table = cfg.deserialize("", CrazyAdvancement.class);
        else{
            if(!(cfg.getRoot() instanceof FileObject root)){
                cfg.close();
                return;
            }

            FileContext<?> ctx = new FileContext<>(cfg.fileRegistry());
            table = CrazyAdvancementsHook.FILE_CRAZY_ADVANCEMENT.deserializeFromFile(
                ctx, root, plugin.key(path)
            );

            if(table != null) table = cfg.fileRegistry().getParsedObjectRegistry().parse(root, ctx, table);
        }
        cfg.close();
        if(table == null) return;
        loaded.accept(table);
    }
}
