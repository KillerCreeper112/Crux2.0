package killercreepr.cruxadvancements.core.config.loader;

import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.core.config.CruxConfigHook;
import killercreepr.cruxconfig.config.bukkit.loader.CfgLoader;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ObjectiveAdvancementCfgLoader extends CfgLoader {
    protected final CruxPlugin plugin;
    protected final Consumer<ObjectiveAdvancement> loaded;
    public ObjectiveAdvancementCfgLoader(CruxPlugin plugin, Consumer<ObjectiveAdvancement> loaded) {
        this.plugin = plugin;
        this.loaded = loaded;
    }

    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        ObjectiveAdvancement table;
        if(path == null) table = cfg.deserialize("", ObjectiveAdvancement.class);
        else{
            if(!(cfg.getRoot() instanceof FileObject root)){
                cfg.close();
                return;
            }

            FileContext<?> ctx = new FileContext<>(cfg.fileRegistry());
            table = CruxConfigHook.FILE_OBJECTIVE_ADVANCEMENT.deserializeFromFile(
                ctx, root, plugin.key(path)
            );

            if(table != null) table = cfg.fileRegistry().getParsedObjectRegistry().parse(root, ctx, table);
        }
        cfg.close();
        if(table == null) return;
        loaded.accept(table);
    }
}
