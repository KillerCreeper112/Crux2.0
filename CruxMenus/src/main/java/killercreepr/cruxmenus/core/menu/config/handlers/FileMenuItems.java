package killercreepr.cruxmenus.core.menu.config.handlers;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class FileMenuItems extends SimpleFileMenuModuled<MenuItems> {
    public FileMenuItems(@NotNull FileMenuHolder<?> menuModule) {
        super(menuModule);
    }

    @Override
    public @Nullable MenuItems deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext) {
        return deserializeFromFile(context, e, menuContext, null);
    }

    public @Nullable MenuItems deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext,
                                                   @Nullable Function<MenuItemHolder, MenuItemHolder> function) {
        return deserializeFromFile(context, e, menuContext, function, null);
    }


    public @Nullable MenuItems deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext,
                                                   @Nullable Function<MenuItemHolder, MenuItemHolder> function,
                                                   @Nullable Function<FileElement, MenuItemHolder> itemHolderParser) {
        if(!(e instanceof FileObject o)){
            if(!(e instanceof FileArray a)){
                if(e instanceof FileGeneric g){
                    if(menuContext == null) throw new IllegalStateException("Cannot attempt to parse reference MenuItems from " + g + " because menuContext is NULL!");
                    FileElement found = menuContext.get(g.getAsString());
                    if(found == null) return null;
                    return deserializeFromFile(context, found, menuContext, function, itemHolderParser);
                }
                return null;
            }
            MenuItems map = MenuItems.items(new TreeMap<>());
            a.forEach(value ->{
                MenuItemHolder item = itemHolderParser == null ? menuModule.getFileMenuItem().deserializeFromFile(context, value, menuContext) :
                    itemHolderParser.apply(value);
                if(item != null){
                    if(function != null) item = function.apply(item);
                    map.add(item);
                }
            });
            return null;
        }
        MenuItems map = MenuItems.items(new TreeMap<>());
        o.forEach((key, value) ->{
            MenuItemHolder item = itemHolderParser == null ? menuModule.getFileMenuItem().deserializeFromFile(context, value, menuContext) :
                itemHolderParser.apply(value);
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
