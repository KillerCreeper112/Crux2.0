package killercreepr.cruxmenus.menu.bukkit.config.handlers;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class FileMenuItems extends FileMenuModuled<MenuItems> {
    public FileMenuItems(@NotNull FileMenuHolder menuModule) {
        super(menuModule);
    }

    @Override
    public @Nullable MenuItems deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext) {
        return deserializeFromFile(context, e, menuContext, null);
    }

    public @Nullable MenuItems deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext,
                                                   @Nullable Function<MenuItemHolder, MenuItemHolder> function) {
        if(!(e instanceof FileObject o)) return null;
        MenuItems map = new MenuItems(new TreeMap<>());
        o.forEach((key, value) ->{
            MenuItemHolder item = menuModule.getYamlMenuItem().deserializeFromFile(context, value, menuContext);
            if(item != null){
                if(function != null) item = function.apply(item);
                map.add(item);
            }
        });
        return map;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "menu_items";
    }
}
