package killercreepr.cruxmenus.core.menu.registry;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.api.registry.Registry;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.registry.KeyedPriorityRegistry;
import killercreepr.crux.core.registry.SimpleRegistry;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxmenus.api.menu.action.MenuAction;
import killercreepr.cruxmenus.api.menu.config.FileDataProvider;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.data.ItemDataParser;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.item.MenuItem;
import killercreepr.cruxmenus.api.menu.item.requirement.ViewCondition;
import killercreepr.cruxmenus.api.menu.module.config.MenuModuleBuilder;
import killercreepr.cruxmenus.api.menu.registry.MenuRegistry;
import killercreepr.cruxmenus.core.menu.config.handlers.*;
import killercreepr.cruxmenus.core.menu.config.loader.MenusCfgLoader;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public class SimpleMenuRegistry implements MenuRegistry {
    private final killercreepr.crux.api.registry.KeyedRegistry<MenuHolder> MENU_HOLDERS = new killercreepr.crux.core.registry.SimpleKeyedRegistry<>(new HashMap<>()){
        @Override
        public @NotNull MenuHolder register(@NotNull Key key, @NotNull MenuHolder value) {
            value.setRegistry(SimpleMenuRegistry.this);
            return super.register(key, value);
        }
    };
    private final killercreepr.crux.api.registry.Registry<MenuAction> MENU_ACTIONS = new SimpleRegistry<>(new HashSet<>());
    private final killercreepr.crux.core.registry.KeyedPriorityRegistry<ItemDataParser> ITEM_DATA_PARSERS = new killercreepr.crux.core.registry.KeyedPriorityRegistry<>();

    protected final @NotNull FormatSerializer format;
    protected final @NotNull FileMenuHolder<?> menuModule;
    protected final @NotNull MenusCfgLoader cfgLoader;
    public SimpleMenuRegistry(@NotNull FormatSerializer format, @NotNull FileMenuHolder<?> menuModule) {
        this.format = format;
        this.menuModule = menuModule;
        this.cfgLoader = new MenusCfgLoader(MENU_HOLDERS, menuModule);
    }

    public SimpleMenuRegistry(@NotNull FormatSerializer format, @NotNull killercreepr.crux.api.registry.KeyedRegistry<MenuModuleBuilder> menuModuleBuilders) {
        this(format, new SimpleFileMenuHolder());
        menuModule.setFileMenuItem(new FileMenuItem(menuModule));
        menuModule.setFileMenuActions(new FileMenuActions(menuModule));
        menuModule.setFileMenuItems(new FileMenuItems(menuModule));
        menuModule.setFileDataExchange(new FileDataExchange(menuModule));
        menuModule.setFileDynamicItem(BukkitCfgHandlers.DYNAMIC_ITEM);
        menuModule.setFileMenuModule(new FileMenuMenuModule(menuModule, menuModuleBuilders));

        menuModule.getFileDataExchange().getDataTypes().register("slot", FileDataProvider.generic(NumberProvider.class));
    }
    @Override
    public @NotNull FormatSerializer getFormat() {
        return format;
    }
    @Override
    public void register(@NotNull FileRegistry registry){
        registry.registerFileHandler(MenuHolder.class, menuModule);
        registry.registerFileHandler(DataExchange.class, menuModule.getFileDataExchange());
        registry.registerFileHandler(MenuItem.class, menuModule.getFileMenuItem());
        registry.registerFileHandler(MenuItems.class, menuModule.getFileMenuItems());
        registry.registerFileHandler(ViewCondition.class, new FileViewCondition());
    }

    @Override
    public void loadConfiguration(@NotNull File folder){
        cfgLoader.loadConfiguration(folder);
    }

    public KeyedRegistry<MenuHolder> menuHolders() {
        return MENU_HOLDERS;
    }

    public Registry<MenuAction> menuActions() {
        return MENU_ACTIONS;
    }

    public KeyedPriorityRegistry<ItemDataParser> itemDataParsers() {
        return ITEM_DATA_PARSERS;
    }

    public @NotNull FileMenuHolder<?>  menuModule() {
        return menuModule;
    }
}
