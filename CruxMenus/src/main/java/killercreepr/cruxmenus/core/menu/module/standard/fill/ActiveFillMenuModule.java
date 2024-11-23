package killercreepr.cruxmenus.core.menu.module.standard.fill;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import killercreepr.cruxmenus.core.menu.module.SimpleActiveMenuModule;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ActiveFillMenuModule extends SimpleActiveMenuModule {
    protected final @NotNull NumberProvider indexes;
    protected final @NotNull MenuItems menuItems;

    public ActiveFillMenuModule(@NotNull String id, @NotNull MenuModule module, @NotNull NumberProvider indexes, @NotNull MenuItems menuItems) {
        super(id, module);
        this.indexes = indexes;
        this.menuItems = menuItems;
    }

    public void fill(@NotNull Menu menu){
        if(!(menu instanceof CfgMenu cfg)) return;
        int index = -1;
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

            cfg.setItems(menuItems, menuContext);
        }
    }
    @Override
    public void refresh(@NotNull Menu menu) {
        super.refresh(menu);
        fill(menu);
    }

    public @NotNull MenuItems getMenuItems() {
        return menuItems;
    }

    public @NotNull NumberProvider getIndexes() {
        return indexes;
    }
}
