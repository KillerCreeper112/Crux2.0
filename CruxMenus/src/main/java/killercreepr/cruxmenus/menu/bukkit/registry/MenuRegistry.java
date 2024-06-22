package killercreepr.cruxmenus.menu.bukkit.registry;

import killercreepr.crux.Crux;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.Registry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.crux.registry.SimpleRegistry;
import killercreepr.crux.tags.format.Format;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import killercreepr.cruxmenus.menu.bukkit.MenuItem;
import killercreepr.cruxmenus.menu.bukkit.actions.MenuAction;
import killercreepr.cruxmenus.menu.bukkit.actions.impl.CommandAction;
import killercreepr.cruxmenus.menu.bukkit.config.FileDataProvider;
import killercreepr.cruxmenus.menu.bukkit.config.handlers.*;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuHolder;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItems;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

public class MenuRegistry {
    public final KeyedRegistry<MenuHolder> MENU_HOLDERS = new SimpleKeyedRegistry<>(new HashMap<>()){
        @Override
        public @NotNull MenuHolder register(@NotNull Key key, @NotNull MenuHolder value) {
            value.setRegistry(MenuRegistry.this);
            return super.register(key, value);
        }
    };
    public final Registry<MenuAction> MENU_ACTIONS = new SimpleRegistry<>(new HashSet<>());

    protected final @NotNull Format format;
    protected final @NotNull FileMenuModule menuModule;
    public MenuRegistry(@NotNull Format format, @NotNull FileMenuModule menuModule) {
        this.format = format;
        this.menuModule = menuModule;

        MENU_ACTIONS.register(new CommandAction(Key.key("test", "command")));
    }

    public MenuRegistry(@NotNull Format format) {
        this(format, new FileMenuModule());
        menuModule.setYamlMenuItem(new FileMenuItem(menuModule));
        menuModule.setYamlMenuActions(new FileMenuActions(menuModule));
        menuModule.setYamlMenuItems(new FileMenuItems(menuModule));
        menuModule.setYamlDataExchange(new FileDataExchange(menuModule));
        menuModule.setYamlDynamicItem(BukkitCfgHandlers.DYNAMIC_ITEM);

        menuModule.getYamlDataExchange().getDataTypes().register("slot", FileDataProvider.generic(NumberProvider.class));
    }

    public @NotNull Format getFormat() {
        return format;
    }

    public void register(@NotNull YamlRegistry registry){
        registry.registerHandler(MenuHolder.class, menuModule);
        registry.registerHandler(DataExchange.class, menuModule.getYamlDataExchange());
        registry.registerHandler(MenuItem.class, menuModule.getYamlMenuItem());
        registry.registerHandler(MenuItems.class, menuModule.getYamlMenuItems());
    }

    public void loadConfiguration(@NotNull File folder){
        File[] list = folder.listFiles();
        if(list==null) return;
        for(File f : list){
            loadConfiguration(new CruxConfig(f));
        }
    }

    public void loadConfiguration(@NotNull CruxConfig cfg){
        MENU_HOLDERS.clear();
        MenuHolder holder = cfg.deserialize(MenuHolder.class, "");
        if(holder == null) return;
        Crux.log(Level.INFO, "Registered crux menu: " + holder.key());
        MENU_HOLDERS.register(holder);
    }
}
