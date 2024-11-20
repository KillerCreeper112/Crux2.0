package killercreepr.cruxmenus.api.menu.registry;

import killercreepr.crux.core.registry.KeyedPriorityRegistry;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.api.registry.Registry;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxmenus.api.menu.action.MenuAction;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.data.ItemDataParser;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.api.menu.module.config.MenuModuleBuilder;
import killercreepr.cruxmenus.core.menu.registry.SimpleMenuRegistry;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface MenuRegistry {
    static MenuRegistry simple(@NotNull FormatSerializer format, @NotNull FileMenuHolder<?> menuModule){
        return new SimpleMenuRegistry(format, menuModule);
    }
    static MenuRegistry simple(@NotNull FormatSerializer format, @NotNull KeyedRegistry<MenuModuleBuilder> menuModuleBuilders){
        return new SimpleMenuRegistry(format, menuModuleBuilders);
    }

    @NotNull FormatSerializer getFormat();

    void register(@NotNull FileRegistry registry);

    void loadConfiguration(@NotNull File folder);

    KeyedRegistry<MenuHolder> menuHolders();

    Registry<MenuAction> menuActions();

    KeyedPriorityRegistry<ItemDataParser> itemDataParsers();

    @NotNull
    FileMenuHolder<?>  menuModule();
}
