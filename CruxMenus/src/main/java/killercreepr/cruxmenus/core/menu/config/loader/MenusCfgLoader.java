package killercreepr.cruxmenus.core.menu.config.loader;

import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.bukkit.loader.CfgLoader;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class MenusCfgLoader extends CfgLoader {
    protected final @NotNull KeyedRegistry<MenuHolder> menuRegistry;
    protected final @Nullable FileMenuHolder<?> fileMenuHolder;
    public MenusCfgLoader(@NotNull KeyedRegistry<MenuHolder> menuRegistry, @Nullable  FileMenuHolder<?>  fileMenuHolder) {
        this.menuRegistry = menuRegistry;
        this.fileMenuHolder = fileMenuHolder;
    }

    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path) {
        MenuHolder table;
        if(path == null){
            table = cfg.deserialize("", MenuHolder.class);
        } else{
            if(!(cfg.getRoot() instanceof FileObject root)) return;
            if(fileMenuHolder == null){
                Crux.log(Level.WARNING, "Cannot load configuration for " + cfg.file().getAbsolutePath() + "!");
                return;
            }

            FileContext<?> ctx = new FileContext<>(cfg.fileRegistry());
            table = fileMenuHolder.deserializeFromFile(
                new FileContext<>(cfg.fileRegistry()), root, Crux.key(path)
            );
            if(table != null) table = cfg.fileRegistry().getParsedObjectRegistry().parse(root, ctx, table);
        }
        if(table == null) return;
        Crux.log(Level.INFO, "Registered crux menu: " + table.key());
        menuRegistry.register(table);
    }
}
