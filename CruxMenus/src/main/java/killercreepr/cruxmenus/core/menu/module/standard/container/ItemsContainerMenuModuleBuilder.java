package killercreepr.cruxmenus.core.menu.module.standard.container;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import killercreepr.cruxmenus.api.menu.module.config.MenuModuleBuilder;
import killercreepr.cruxmenus.core.menu.config.handlers.SimpleFileMenuModuled;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class ItemsContainerMenuModuleBuilder extends SimpleFileMenuModuled<MenuModule> implements MenuModuleBuilder {
    protected final @NotNull Key key;
    public ItemsContainerMenuModuleBuilder(@NotNull FileMenuHolder<?> menuModule, @NotNull Key key) {
        super(menuModule);
        this.key = key;
    }

    @Override
    public @Nullable MenuModule build(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable FileObject menuContext) {
        return deserializeFromFile(ctx, e, menuContext);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }


    //- type: quest_1
    //  base_items: quest_items
    //  base_item:
    //    slot: 11
    //    data:
    //      advancement: usurvive:fishing/catch_fish_1
    //  items:
    //    - view_requirement: 'IF("<player_has_completed_advancement:"<advancement_manager_key>":"<advancement_parent>">" == "true", "false", "true")'
    //      item:
    //        material: stone
    @Override
    public @Nullable MenuModule deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable FileObject menuCtx) {
        if(!(e instanceof FileObject o)) return null;
        String id = o.getObject(String.class, "id");
        if(id==null) id = UUID.randomUUID().toString();
        String baseItemsString = o.getObject(String.class, "base_items");
        //todo make it possible to configure multiple base items
        MenuItemHolder defaultBaseItem = menuModule.getFileMenuItem().deserializeFromFile(
            ctx, o.get("base_item"), menuCtx
        );
        List<MenuItemHolder> defaultBaseItems = defaultBaseItem == null ? null : List.of(defaultBaseItem);

        Function<FileElement, MenuItemHolder> parser = value ->menuModule.getFileMenuItem().deserializeFromFile(ctx, value, menuCtx, defaultBaseItems);
        MenuItems baseItems;
        if(baseItemsString != null){
            baseItems = menuModule.getFileMenuItems().deserializeFromFile(ctx, menuCtx.get(baseItemsString), menuCtx,
                null, parser);
        }else baseItems = MenuItems.items(new TreeMap<>());
        final MenuItems finalBaseItems = baseItems == null ? MenuItems.items(new TreeMap<>()) : baseItems;

        Collection<MenuItems> items = new ArrayList<>();
        FileElement itemsElement = o.get("items");
        if(itemsElement instanceof FileArray a){
            a.forEach(value ->{
                MenuItems menuItems = menuModule.getFileMenuItems().deserializeFromFile(ctx, value, menuCtx, null, parser);
                if(menuItems != null) items.add(menuItems);
            });
        }else{
            MenuItems menuItems = menuModule.getFileMenuItems().deserializeFromFile(ctx, itemsElement, menuCtx, null, parser);
            if(menuItems != null) items.add(menuItems);
        }
        for(MenuItems i : items){
            i.forEach(menuItemsItems ->{
                menuItemsItems.forEach(finalBaseItems::add);
            });
        }
        return new ItemsContainerMenuModule(id, this, defaultBaseItem, finalBaseItems);
    }
}
