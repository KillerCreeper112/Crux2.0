package killercreepr.cruxmenus;

import killercreepr.crux.Crux;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxmenus.command.CruxMenusCommands;
import killercreepr.cruxmenus.command.argument.CruxMenuHolderArgument;
import killercreepr.cruxmenus.command.argument.CruxMenusArguments;
import killercreepr.cruxmenus.menu.bukkit.actions.standard.StandardMenuActions;
import killercreepr.cruxmenus.menu.bukkit.listener.MenuListener;
import killercreepr.cruxmenus.menu.bukkit.module.config.MenuModuleBuilder;
import killercreepr.cruxmenus.menu.bukkit.registry.MenuRegistry;
import killercreepr.cruxmenus.tags.MenuItemHolderHook;
import org.jetbrains.annotations.NotNull;

public class CruxMenusModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_MENUS;
    protected final @NotNull MenuRegistry menuRegistry;
    protected final @NotNull KeyedRegistry<MenuModuleBuilder> menuModuleRegistry;
    public CruxMenusModule(@NotNull MenuRegistry registry, @NotNull KeyedRegistry<MenuModuleBuilder> menuModuleRegistry){
        this.menuRegistry = registry;
        this.menuModuleRegistry = menuModuleRegistry;
    }

    public CruxMenusModule() {
        this.menuModuleRegistry = new SimpleKeyedRegistry<>();
        this.menuRegistry = new MenuRegistry(Crux.FORMAT, menuModuleRegistry);
        StandardMenuActions.buildActions().forEach(menuRegistry.MENU_ACTIONS::register);
    }

    public @NotNull MenuRegistry menuRegistry() {
        return menuRegistry;
    }

    public @NotNull KeyedRegistry<MenuModuleBuilder> menuModuleRegistry() {
        return menuModuleRegistry;
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
        menuRegistry.register(CfgRegistries.JSON);
        menuRegistry.loadConfiguration(new CruxFolder(plugin, "menus").file());
    }
}
