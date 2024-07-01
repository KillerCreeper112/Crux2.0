package killercreepr.cruxmenus;

import killercreepr.crux.Crux;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxmenus.command.CruxMenusCommands;
import killercreepr.cruxmenus.command.argument.CruxMenuHolderArgument;
import killercreepr.cruxmenus.command.argument.CruxMenusArguments;
import killercreepr.cruxmenus.menu.bukkit.actions.standard.StandardMenuActions;
import killercreepr.cruxmenus.menu.bukkit.listener.MenuListener;
import killercreepr.cruxmenus.menu.bukkit.registry.MenuRegistry;
import killercreepr.cruxmenus.tags.MenuItemHolderHook;
import org.jetbrains.annotations.NotNull;

public class CruxMenusModule implements CruxModule {
    public static final String NAMESPACE = "CruxMenus";
    protected final @NotNull MenuRegistry menuRegistry;
    public CruxMenusModule(@NotNull MenuRegistry registry){
        this.menuRegistry = registry;
    }

    public CruxMenusModule() {
        this(new MenuRegistry(Crux.FORMAT));
        StandardMenuActions.buildActions().forEach(menuRegistry.MENU_ACTIONS::register);
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
        CruxMenusArguments.setMenuHolder(new CruxMenuHolderArgument(this));
        CruxMenusCommands.register(plugin);
        plugin.registerListeners(
                new MenuListener(plugin)
        );

        menuRegistry.getFormat().tags().register(new MenuItemHolderHook());
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        menuRegistry.register(CfgRegistries.YAML);
        menuRegistry.loadConfiguration(new CruxFolder(plugin, "menus").file());
    }
}
