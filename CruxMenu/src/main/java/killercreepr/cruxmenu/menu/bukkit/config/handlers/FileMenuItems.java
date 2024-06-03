package killercreepr.cruxmenu.menu.bukkit.config.handlers;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenu.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxmenu.menu.bukkit.holder.MenuItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.TreeMap;

public class FileMenuItems extends FileModuled<MenuItems> {
    public FileMenuItems(@NotNull FileMenuModule menuModule) {
        super(menuModule);
    }

    @Override
    public @Nullable MenuItems deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext) {
        if(!(e instanceof FileObject o)) return null;
        MenuItems map = new MenuItems(new TreeMap<>());
        o.forEach((key, value) ->{
            MenuItemHolder item = menuModule.getYamlMenuItem().deserializeFromFile(context, value, menuContext);
            if(item != null) map.add(item);
        });
        return map;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "menu_items";
    }
}
