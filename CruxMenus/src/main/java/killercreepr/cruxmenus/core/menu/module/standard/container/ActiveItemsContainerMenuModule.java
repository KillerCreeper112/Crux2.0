package killercreepr.cruxmenus.core.menu.module.standard.container;

import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import killercreepr.cruxmenus.core.menu.module.SimpleActiveMenuModuled;
import org.jetbrains.annotations.NotNull;

public class ActiveItemsContainerMenuModule extends SimpleActiveMenuModuled {
    protected final @NotNull MenuItems menuItems;

    public ActiveItemsContainerMenuModule(@NotNull String id, @NotNull MenuModule module, @NotNull MenuItems menuItems) {
        super(id, module);
        this.menuItems = menuItems;
    }

    public void fill(@NotNull Menu menu){
        if(!(menu instanceof CfgMenu cfg)) return;

        MenuContext menuContext = MenuContext.context(cfg,
            cfg.info(), cfg.buildTags().addAll(buildTags(
                menu, cfg.getHolder().getRegistry().getFormat().tags()
            )));
        cfg.setItems(menuItems, menuContext);
    }
    @Override
    public void refresh(@NotNull Menu menu) {
        super.refresh(menu);
        fill(menu);
    }

    public @NotNull MenuItems getMenuItems() {
        return menuItems;
    }
}
