package killercreepr.cruxmenu;

import killercreepr.crux.Crux;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxmenu.menu.bukkit.listener.MenuListener;
import killercreepr.cruxmenu.menu.bukkit.registry.MenuRegistry;
import org.jetbrains.annotations.NotNull;

public class CruxMenu implements CruxModule {
    public static final String NAMESPACE = "CruxMenu";
    protected final @NotNull MenuRegistry menuRegistry;
    public CruxMenu(@NotNull MenuRegistry registry){
        this.menuRegistry = registry;
    }

    public CruxMenu() {
        this(new MenuRegistry(Crux.FORMAT));
    }

    public @NotNull MenuRegistry menuRegistry() {
        return menuRegistry;
    }

    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
                new MenuListener(plugin)
        );
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        menuRegistry.register(CfgRegistries.YAML);
        menuRegistry.loadConfiguration(new CruxFolder(plugin, "menus").file());
    }
}
