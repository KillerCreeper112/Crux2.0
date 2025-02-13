package killercreepr.cruxmenus.core.menu.module.standard.fill.simple;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.item.MenuItem;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import killercreepr.cruxmenus.core.menu.module.SimpleActiveMenuModuled;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ActiveSimpleFillMenuModule extends SimpleActiveMenuModuled {
    protected final @NotNull NumberProvider indexes;
    protected final @NotNull MenuItemHolder menuItem;
    protected final @Nullable List<Integer> cachedIndexes;

    public ActiveSimpleFillMenuModule(@NotNull String id, @NotNull MenuModule module, @NotNull NumberProvider indexes, @NotNull MenuItemHolder menuItem, @Nullable List<Integer> cachedIndexes) {
        super(id, module);
        this.indexes = indexes;
        this.menuItem = menuItem;
        this.cachedIndexes = cachedIndexes;
    }

    public void fill(@NotNull Menu menu){
        if(!(menu instanceof CfgMenu cfg)) return;
        int index = -1;

        if(cachedIndexes != null){
            for(int slot : cachedIndexes){
                index++;
                int listIndex = index;
                MenuContext menuContext = MenuContext.context(cfg,
                    cfg.info(), cfg.buildTags().addAll(buildTags(
                        menu, cfg.getHolder().getRegistry().getFormat().tags()
                    )).addAll(
                        Tag.parsed(MenuModule.buildTag(id, "slot"), slot+""),
                        Tag.parsed(MenuModule.buildTag(id, "index"), index+""),
                        Tag.parsed(MenuModule.buildTag(id, "list_index"), listIndex+"")
                    ));
                cfg.setItem(menuItem, menuContext);
            }
            return;
        }

        List<Number> indexes = this.indexes.sampleList();

        for(Number number : indexes){
            int slot = number.intValue();
            index++;
            int listIndex = index;
            MenuContext menuContext = MenuContext.context(cfg,
                cfg.info(), cfg.buildTags().addAll(buildTags(
                    menu, cfg.getHolder().getRegistry().getFormat().tags()
                )).addAll(
                    Tag.parsed(MenuModule.buildTag(id, "slot"), slot+""),
                    Tag.parsed(MenuModule.buildTag(id, "index"), index+""),
                    Tag.parsed(MenuModule.buildTag(id, "list_index"), listIndex+"")
                ));
            cfg.setItem(menuItem, menuContext);
        }
    }
    @Override
    public void refresh(@NotNull Menu menu) {
        super.refresh(menu);
        fill(menu);
    }

    public @NotNull MenuItemHolder getMenuItem() {
        return menuItem;
    }

    public @NotNull NumberProvider getIndexes() {
        return indexes;
    }
}
